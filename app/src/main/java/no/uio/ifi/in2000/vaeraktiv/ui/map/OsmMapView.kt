package no.uio.ifi.in2000.vaeraktiv.ui.map

import android.content.Context
import android.graphics.Color
import android.preference.PreferenceManager
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.places.NearbyPlaceSuggestion
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
//import org.osmdroid.bonuspack.utils.PolylineEncoder

@Composable
fun OsmMapView(
    context: Context,
    places: List<NearbyPlaceSuggestion>,
    routes: List<RouteSuggestion>
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
            places.forEach { place ->
                place.coordinates?.let { (lat, lon) ->
                    Marker(mapView).apply {
                        position = GeoPoint(lat, lon)
                        title = place.displayName
                        mapView.overlays.add(this)
                    }
                }
            }
            routes.forEach { route ->
                val points = decodePolyline(route.polyline)
                //val points: List<GeoPoint> = PolylineEncoder.decode(route.polyline, 5, false)
                Log.d("OsmMapView", "route=${route.id} points=${points.size}")
                val lineOverlay = Polyline().apply {
                    title = route.name
                    outlinePaint.apply {
                        strokeWidth = 10f
                        color = Color.RED
                    }
                    setPoints(points)
                }
                mapView.overlayManager.add(lineOverlay)
            }
            mapView.invalidate()
        }
    )
}

// Polyline decoder (Google polyline format)
fun decodePolyline(encoded: String): List<GeoPoint> {
    val points = mutableListOf<GeoPoint>()
    var index = 0
    var lat = 0
    var lng = 0

    while (index < encoded.length) {
        var result = 0
        var shift = 0
        var b: Int

        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)

        val dlat = if (result and 1 != 0) (result.inv() shr 1) else (result shr 1)
        lat += dlat

        result = 0
        shift = 0

        do {
            b = encoded[index++].code - 63
            result = result or ((b and 0x1f) shl shift)
            shift += 5
        } while (b >= 0x20)

        val dlng = if (result and 1 != 0) (result.inv() shr 1) else (result shr 1)
        lng += dlng

        points.add(GeoPoint(lat / 1e5, lng / 1e5))
    }

    return points
}