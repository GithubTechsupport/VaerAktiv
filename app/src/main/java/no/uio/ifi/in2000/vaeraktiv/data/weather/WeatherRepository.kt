package no.uio.ifi.in2000.vaeraktiv.data.weather

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavLocation
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

    private var locations = mutableMapOf( ("Bergen" to ("62" to "14")), ("Oslo" to ("60" to "10")),) //bare for testing

    suspend fun getFavoriteLocationsData(): MutableList<FavoriteLocation> {
        val locationsData:MutableList<FavoriteLocation> = mutableListOf()
        for (key in locations.keys){
            val pair = locations[key]
            if(pair != null){
                val (lat, lon) = pair
                val forecast = locationForecastRepository.getForecast(lat, lon)
                val data = forecast?.properties?.timeseries?.get(0)?.data
                val weatherData = FavoriteLocation(
                    name = key.toString(),
                    iconDesc = data?.next12Hours?.summary?.symbolCode.toString(),
                    shortDesc = "AI",
                    highestTemp = data?.next6Hours?.details?.airTemperatureMax.toString(),
                    lowestTemp = data?.next6Hours?.details?.airTemperatureMin.toString(),
                    wind = data?.instant?.details?.windSpeed.toString(),
                    downPour = data?.next6Hours?.details?.precipitationAmount.toString(),
                    uv = data?.instant?.details?.ultravioletIndexClearSky.toString()
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