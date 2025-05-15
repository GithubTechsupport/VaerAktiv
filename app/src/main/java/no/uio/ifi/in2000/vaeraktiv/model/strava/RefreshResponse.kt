package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.SerialName

/**
 * OAuth token refresh response from Strava.
 *
 * @param tokenType the token type (e.g., "Bearer")
 * @param accessToken new access token string
 * @param refreshToken new refresh token string
 * @param expiresAt epoch seconds when access token expires
 * @param expiresIn seconds until the access token expires
 */

@kotlinx.serialization.Serializable
data class RefreshResponse(
    @SerialName("token_type")
    val tokenType: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String,
    @SerialName("expires_at")
    val expiresAt: Long,
    @SerialName("expires_in")
    val expiresIn: Long,
)