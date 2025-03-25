package no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise

import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunData
import javax.inject.Inject

class SunriseRepository @Inject constructor(private val sunriseDataSource: SunriseDataSource){

    suspend fun getSunriseTime(
        lat: String,
        lon: String,
        date: String
    ): List<String> {
        val sunriseInfo: SunData? = sunriseDataSource.getSunrise(lat, lon, date)
        return listOf(sunriseInfo?.properties?.sunrise?.time?: "No sunrise data found", sunriseInfo?.properties?.sunset?.time?: "No sunset data found")
    }
}