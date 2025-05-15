package no.uio.ifi.in2000.vaeraktiv.data.strava

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import no.uio.ifi.in2000.vaeraktiv.model.strava.ExplorerResponse
import no.uio.ifi.in2000.vaeraktiv.model.strava.ExplorerSegment
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named


class StravaDatasource @Inject constructor(
    @Named("prettyPrint-isLenient-ignoreUnknownKeys-Client")
    private val networkClient: NetworkClient,
    private val authManager: StravaAuthManager
) {
    suspend fun fetchPopularRunSegments(
        swLat: Double, swLng: Double, // Defines the area where the run segments should be.
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