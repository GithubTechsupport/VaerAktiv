package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.SerialName

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