package no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise

import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunData
import javax.inject.Inject

/**
 * Provides formatted sunrise and sunset times from SunData.
 */
class SunriseRepository @Inject constructor(
    private val sunriseDataSource: SunriseDataSource
) {

    /**
     * Returns a list containing sunrise and sunset time strings.
     */
    suspend fun getSunriseTime(
        lat: String,
        lon: String,
        date: String
    ): List<String> {
        val sunriseInfo: SunData? = sunriseDataSource.getSunrise(lat, lon, date)
        return listOf(sunriseInfo?.properties?.sunrise?.time?: "No sunrise data found", sunriseInfo?.properties?.sunset?.time?: "No sunset data found")
    }
}
