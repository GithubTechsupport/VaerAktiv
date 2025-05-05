package no.uio.ifi.in2000.vaeraktiv.model.strava

interface TokenStorage {
    suspend fun getTokens(): StravaAuthTokens?
    suspend fun saveTokens(tokens: StravaAuthTokens)
}