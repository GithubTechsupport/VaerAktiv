package no.uio.ifi.in2000.EmptyApplication.data.sunrise

import no.uio.ifi.in2000.EmptyApplication.model.sunrise.SunData

class SunriseRepository {
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource()

    suspend fun getSunriseTime(
        lat: String,
        lon: String,
        date: String
    ): List<String> {
        val sunriseInfo: SunData? = sunriseDataSource.getSunrise(lat, lon, date)
        return listOf(sunriseInfo?.properties?.sunrise?.time?: "No sunrise data found", sunriseInfo?.properties?.sunset?.time?: "No sunset data found")
    }
}