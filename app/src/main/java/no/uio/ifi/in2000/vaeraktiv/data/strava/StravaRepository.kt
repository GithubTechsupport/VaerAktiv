package no.uio.ifi.in2000.vaeraktiv.data.strava

import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject

class StravaRepository @Inject constructor(
    private val datasource: StravaDatasource
) {
    /**
     * Retrieves and transforms Strava running segments into RouteSuggestions.
     */
    suspend fun getRouteSuggestions(
        center: Pair<Double, Double>,
        width: Double = 0.05,
        height: Double = 0.05
    ): RoutesSuggestions {
        val swLat = center.first - height
        val swLng = center.second - width
        val neLat = center.first + height
        val neLng = center.second + width

        val segments = datasource.fetchPopularRunSegments(swLat, swLng, neLat, neLng)
        val suggestions = segments.map { segment ->
            RouteSuggestion(
                id = segment.id.toString(),
                name = segment.name,
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

suspend fun main() {
    val networkClient = NetworkClient(Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    })
    val source = StravaDatasource(networkClient)
    val repo = StravaRepository(source)
    println(repo.getRouteSuggestions(Pair(59.9111, 10.7533)))
}