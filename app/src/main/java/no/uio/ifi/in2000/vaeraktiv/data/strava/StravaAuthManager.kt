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
    private val initialRefreshToken = "YOUR_STRAVA_REFRESH_TOKEN"
    private val clientId = "YOUR_STRAVA_CLIENT_ID"
    private val clientSecret = "YOUR_STRAVA_CLIENT_SECRET"

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

            val resp = networkClient.ktorHttpClient.post(tokenUrl) {
                parameter("client_id", clientId)
                parameter("client_secret", clientSecret)
                parameter("grant_type", "refresh_token")
                parameter("refresh_token", refreshToken)
            }.body<RefreshResponse>()
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