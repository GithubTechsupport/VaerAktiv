package no.uio.ifi.in2000.vaeraktiv.data.strava

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

// Data classes matching Strava's segments/explore response
@Serializable
data class ExplorerResponse(
    val segments: List<ExplorerSegment>
)

@Serializable
data class ExplorerSegment(
    val id: Long,
    val name: String,
    val distance: Double,
    @SerialName("avg_grade") val averageGrade: Double,
    @SerialName("elev_difference") val elevationGain: Double,
    @SerialName("points") val polyline: String,
    @SerialName("start_latlng") val startPosition: List<Double>,
    @SerialName("end_latlng") val endPosition: List<Double>
)

/**
 * StravaDatasource handles HTTP calls to Strava's API.
 * @param client Preconfigured Ktor HttpClient with JSON support
 * @param accessToken OAuth2 bearer token for Strava API
 */

class StravaDatasource @Inject constructor(
    @Named("prettyPrint-isLenient-ignoreUnknownKeys-Client")
    private val networkClient: NetworkClient,
    private val authManager: StravaAuthManager
) {
    suspend fun fetchPopularRunSegments(
        swLat: Double, swLng: Double,
        neLat: Double, neLng: Double
    ): List<ExplorerSegment> {
        val token = authManager.getAccessToken()
        val bounds = listOf(swLat, swLng, neLat, neLng).joinToString(",")
        val url = "https://www.strava.com/api/v3/segments/explore"

        val resp = networkClient.ktorHttpClient.get(url) {
            header(HttpHeaders.Authorization, "Bearer $token")
            parameter("bounds", bounds)
            parameter("activity_type", "running")
        }.body<ExplorerResponse>()

        return resp.segments
    }
}