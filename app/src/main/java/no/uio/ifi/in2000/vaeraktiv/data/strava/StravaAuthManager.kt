package no.uio.ifi.in2000.vaeraktiv.data.strava

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import no.uio.ifi.in2000.vaeraktiv.model.strava.RefreshResponse
import no.uio.ifi.in2000.vaeraktiv.model.strava.StravaAuthTokens
import no.uio.ifi.in2000.vaeraktiv.model.strava.TokenStorage
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

class StravaAuthManager @Inject constructor(
    @Named("prettyPrint-isLenient-ignoreUnknownKeys-Client")
    private val networkClient: NetworkClient,
    private val storage: TokenStorage,
) {
    private val tokenUrl = "https://www.strava.com/oauth/token"
    private val initialRefreshToken = "cee197301f38b58044bcb4b573f53682c8a5f6d0"
    private val clientId = "156834"
    private val clientSecret = "59676a32980c48e129b589b2d076e4a23f619e4b"

    suspend fun getAccessToken(): String {
        val stored = storage.getTokens()
        val tokens = if (stored == null) {
            // first run: bootstrap from the one‐time secret
            val t = refresh(initialRefreshToken)
            storage.saveTokens(t)
            t
        } else if (stored.isExpired()) {
            val t = refresh(stored.refreshToken)
            storage.saveTokens(t)
            t
        } else {
            stored
        }
        return tokens.accessToken
    }

    private suspend fun refresh(refreshToken: String?): StravaAuthTokens {
        return try {
            // Make a POST request with the required parameters
            val resp = networkClient.ktorHttpClient.post(tokenUrl) {
                parameter("client_id", clientId)
                parameter("client_secret", clientSecret)
                parameter("grant_type", "refresh_token")
                parameter("refresh_token", refreshToken)
            }.body<RefreshResponse>() // Parse the response into a RefreshResponse object
            StravaAuthTokens(
                accessToken = resp.accessToken,
                refreshToken = resp.refreshToken,
                expiresAt = resp.expiresAt
            )
        } catch (e: Exception) {
            Log.e("StravaAuthManager", "Failed to refresh strava token: ", e)
            throw e
        }
    }
}