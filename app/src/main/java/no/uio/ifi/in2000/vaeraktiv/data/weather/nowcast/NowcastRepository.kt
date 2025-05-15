package no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast

import no.uio.ifi.in2000.vaeraktiv.model.nowcast.NowcastResponse
import javax.inject.Inject

/**
 * Caches nowcast responses and provides retrieval by coordinates.
 */
class NowcastRepository @Inject constructor(nowcastDataSource: NowcastDataSource) {
    private val loc = nowcastDataSource
    private var forecasts: MutableMap<Pair<String, String>, NowcastResponse?> = mutableMapOf()
    private var response: NowcastResponse? = null

    /**
     * Fetches fresh nowcast data and updates the cache.
     */
    suspend fun getUpdate(lat: String, lon: String): NowcastResponse? {
        response = loc.getResponse("https://api.met.no/weatherapi/nowcast/2.0/complete?lat=$lat&lon=$lon")
        forecasts[Pair(lat, lon)] = response
        return response
    }

    /**
     * Returns cached nowcast or fetches a new one if not present.
     */
    suspend fun getForecast(lat: String, lon: String): NowcastResponse? {
        if (Pair(lat, lon) !in forecasts.keys) {
            return getUpdate(lat, lon)
        }

        return forecasts[Pair(lat, lon)]
    }
}
