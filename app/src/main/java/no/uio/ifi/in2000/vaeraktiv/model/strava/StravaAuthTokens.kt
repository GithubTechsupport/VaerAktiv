package no.uio.ifi.in2000.vaeraktiv.model.strava

/**
 * Holds Strava OAuth tokens and expiry information.
 *
 * @param accessToken current access token
 * @param refreshToken token used to refresh the access token
 * @param expiresAt epoch seconds when the access token expires
 */
data class StravaAuthTokens (
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: Long
) {
    /**
     * Checks whether the access token has expired.
     *
     * @return true if current time (s) ≥ expiresAt
     */
    fun isExpired(): Boolean =
        System.currentTimeMillis() / 1000 >= expiresAt
}