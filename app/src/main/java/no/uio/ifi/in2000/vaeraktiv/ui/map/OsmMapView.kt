package no.uio.ifi.in2000.vaeraktiv.ui.map

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.toDrawable
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlacesActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline


// Helper: red circle + white inner circle
private fun createPlaceIcon(context: Context, sizePx: Int = 48): BitmapDrawable {
    val bmp = createBitmap(sizePx, sizePx)
    val canvas = Canvas(bmp)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.color = Color.RED
    canvas.drawCircle(sizePx/2f, sizePx/2f, sizePx/2f, paint)
    paint.color = Color.WHITE
    canvas.drawCircle(sizePx/2f, sizePx/2f, sizePx/4f, paint)
    return bmp.toDrawable(context.resources)
}

// Helper: red circle + white flag triangle
private fun createStartFlagIcon(context: Context, sizePx: Int = 60): BitmapDrawable {
    val bmp = createBitmap(sizePx, sizePx)
    val canvas = Canvas(bmp)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // background circle
    paint.color = Color.RED
    canvas.drawCircle(sizePx/2f, sizePx/2f, sizePx/2f, paint)

    // pole dimensions
    paint.color = Color.WHITE
    val poleWidth = sizePx * 0.07f
    val poleLeft = sizePx * 0.28f
    val poleTop = sizePx * 0.28f
    val poleBottom = sizePx * 0.72f
    canvas.drawRect(poleLeft, poleTop, poleLeft + poleWidth, poleBottom, paint)

    // flag triangle
    val path = Path().apply {
        val left = sizePx * 0.35f
        val top = sizePx * 0.3f
        val w = sizePx * 0.3f
        val h = sizePx * 0.2f
        moveTo(left, top)
        lineTo(left + w, top + h/2)
        lineTo(left, top + h)
        close()
    }
    canvas.drawPath(path, paint)

    return bmp.toDrawable(context.resources)
}

@Composable
fun OsmMapView(
    context: Context,
    places: List<PlacesActivitySuggestion>,
    routes: List<StravaActivitySuggestion>,
    decodePolyline: (String) -> List<GeoPoint>,
    selectedActivityPoints: List<GeoPoint>?,
    onZoomHandled: () -> Unit // <-- add this callback
) {

    AndroidView(
        factory = { ctx ->

            // Initialize OSMdroid configuration
            val appCtx = ctx.applicationContext
            Configuration.getInstance()
                .load(appCtx, PreferenceManager.getDefaultSharedPreferences(appCtx))
            Configuration.getInstance().userAgentValue = appCtx.packageName

            MapView(appCtx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(13.0)
                // center on first place or default
                val center = places.firstOrNull()?.coordinates
                    ?.let { GeoPoint(it.first, it.second) }
                    ?: GeoPoint(59.9111, 10.7533)
                controller.setCenter(center)
            }
        },
        update = { mapView ->
            mapView.overlays.clear()
            mapView.controller.setZoom(16.0)
            places.forEach { place ->
                place.coordinates.let { (lat, lon) ->
                    Marker(mapView).apply {
                        position = GeoPoint(lat, lon)
                        title = place.activityName
                        // tint the default marker icon red
                        icon = icon?.mutate()
                        icon?.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN)
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    }.also { mapView.overlays.add(it) }
                }
            }
            routes.forEach { route ->
                try {
                    val polylinePoints = decodePolyline(route.polyline)

                    Polyline().apply {
                        title = route.activityName
                        outlinePaint.apply {
                            strokeWidth = 10f
                            color = Color.RED
                        }
                        setPoints(polylinePoints)
                    }.also { mapView.overlays.add(it) }

                    if (polylinePoints.isNotEmpty()) {
                        // START: red pin with white flag
                        Marker(mapView).apply {
                            position = polylinePoints.first()
                            title = "${route.routeName} (Start)"
                            icon = createStartFlagIcon(mapView.context)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }.also { mapView.overlays.add(it) }

                        // END: larger red circle overlay
                        Marker(mapView).apply {
                            position = polylinePoints.last()
                            title = "${route.routeName} (Start)"
                            icon = createPlaceIcon(mapView.context)
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        }.also { mapView.overlays.add(it) }
                    }
                } catch (e: Exception) {
                    Log.e("OsmMapView", "Error decoding polyline: $e")
                }
            }
            selectedActivityPoints?.let { pts ->
                mapView.post {
                    if (pts.size == 1) {
                        mapView.controller.animateTo(pts.first())
                    } else if (pts.isNotEmpty()) {
                        val bbox = BoundingBox.fromGeoPoints(pts)
                        mapView.zoomToBoundingBox(bbox, true, 50)
                    }
                    onZoomHandled()
                }
            }

            mapView.invalidate()
        }
    )
}