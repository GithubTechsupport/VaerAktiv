package no.uio.ifi.in2000.vaeraktiv.model.strava

data class StravaAuthTokens (
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
) {
    fun isExpired(): Boolean =
        System.currentTimeMillis() / 1000 >= expiresAt
}