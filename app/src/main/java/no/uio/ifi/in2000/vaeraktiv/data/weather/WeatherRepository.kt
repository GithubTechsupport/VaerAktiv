package no.uio.ifi.in2000.vaeraktiv.data.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.loactionforecast.Forecast
import no.uio.ifi.in2000.vaeraktiv.model.loactionforecast.Instant
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val metAlertsRepository: MetAlertsRepository?,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository?, //bare nullable for testing
) {
    //private var locations: MutableMap<String, Pair<String, String>> = mutableMapOf()
    init {
        /*locationFile = favoriteLocationsRepo.getLocations() //forventer et fil objekt
        val reader = locationsFile.bufferedReader()

        reader.forEachLine { line ->
            val parts = line.split(" ")
            if (parts.size >= 3) {
                val name = parts[0]
                val lat = parts[1]
                val lon = parts[2]
                locations[name] = Pair(lat, lon)
                locationForecastRepository.getUpdate(lat, lon)
        */
    }

    private var locations = mutableMapOf(("Oslo" to ("60" to "10")), ("Bergen" to ("62" to "14"))) //bare for testing
    suspend fun getFavoriteLocationsData(): MutableList<WeatherData> {
        val locationsData:MutableList<WeatherData> = mutableListOf()
        for (key in locations.keys){
            val pair = locations[key]
            if(pair != null){
                val (lat, lon) = pair
                val forecast = locationForecastRepository.getForecast(lat, lon)
                val data = forecast?.properties?.timeseries?.get(0)?.data
                val weatherData = WeatherData(
                    location = key,
                    weatherCode = data?.next12Hours?.summary?.symbolCode,
                    maxTemp = data?.next6Hours?.details?.airTemperatureMax.toString(),
                    minTemp = data?.next6Hours?.details?.airTemperatureMin.toString(),
                    windSpeed = data?.instant?.details?.windSpeed.toString(),
                    precipitation = data?.next6Hours?.details?.precipitationAmount.toString()
                )
                locationsData.add(weatherData)
            }
        }
        return locationsData
    }


}

suspend fun main() {
    val d = LocationForecastDataSource()
    val loc = LocationForecastRepository(d)
    val w = WeatherRepository(null, loc, null)
    val test = w.getFavoriteLocationsData()
    println("${test[0]} ${test[1]}")
}

@Serializable
data class WeatherData(
    val location: String? = null,

    val weatherCode: String? = null,

    val maxTemp: String? = null,

    val minTemp: String? = null,

    val windSpeed: String? = null,

    val precipitation: String? = null
)