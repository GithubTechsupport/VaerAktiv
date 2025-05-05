package no.uio.ifi.in2000.vaeraktiv.data.strava

import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import javax.inject.Inject

class StravaRepository @Inject constructor(
    private val datasource: StravaDatasource
) {
    /**
     * Retrieves and transforms Strava running segments into RouteSuggestions.
     */
    suspend fun getRouteSuggestions(
        location: Location,
        width: Double = 0.05,
        height: Double = 0.05
    ): RoutesSuggestions {
        val lat = location.lat.toDouble()
        val lon = location.lon.toDouble()
        val swLat = lat - height
        val swLng = lon - width
        val neLat = lat + height
        val neLng = lon + width

        val segments = datasource.fetchPopularRunSegments(swLat, swLng, neLat, neLng)
        val suggestions = segments.map { segment ->
            RouteSuggestion(
                id = segment.id.toString(),
                routeName = segment.name,
                distance = segment.distance,
                elevationGain = segment.elevationGain,
                averageGrade = segment.averageGrade,
                polyline = segment.polyline,
                source = RouteSource.STRAVA,
                startPosition = Pair(segment.startPosition[0], segment.startPosition[1]),
                endPosition = Pair(segment.endPosition[0], segment.endPosition[1])
            )
        }
        return RoutesSuggestions(suggestions)
    }
}