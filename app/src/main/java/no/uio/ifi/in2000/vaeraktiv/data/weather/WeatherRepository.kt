package no.uio.ifi.in2000.vaeraktiv.data.weather

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Features
import no.uio.ifi.in2000.vaeraktiv.model.ui.TodaysWeatherData

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val metAlertsRepository: MetAlertsRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository, //bare nullable for testing
    private val nowcastRepository: NowcastRepository

) {
    private var locations: MutableMap<String, Pair<String, String>> = mutableMapOf()




//    suspend fun getUpdates(locationsList: List<String>) {
//        val locationsList = favoriteLocationRepo.getAllLocations()
//
//        locationsList.forEach { line ->
//            val parts = line.split(",")
//            if (parts.size >= 3) {
//                val name = parts[0]
//                val lat = parts[1]
//                val lon = parts[2]
//                locations[name] = Pair(lat, lon)
//
//                locationForecastRepository.getUpdate(lat, lon)
//            }
//        }
//    }


    suspend fun getFavoriteLocationsData(locationsList: List<String>): MutableList<FavoriteLocation> {
        val locationsData:MutableList<FavoriteLocation> = mutableListOf()
        locationsList.forEach { line ->
            val parts = line.split(",")
            val placeName = parts[0]
            val lat = parts[1]
            val lon = parts[2]
            val forecast = locationForecastRepository.getForecast(lat, lon)
            val data = forecast?.properties?.timeseries?.get(0)?.data
            val weatherData = FavoriteLocation(
                name = placeName,
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
        return locationsData
    }

    suspend fun getAlert(lat: String, lon: String): Features? {
        val response = metAlertsRepository.getAlertForLocation(lat, lon)
        return response
    }

    suspend fun getTodaysWeatherData(lat: String, lon: String): TodaysWeatherData {
        val forecast = locationForecastRepository.getForecast(lat, lon)
        val locationData = forecast?.properties?.timeseries?.get(0)?.data
        val nowcast = nowcastRepository.getForecast(lat, lon)
        val nowcastData = nowcast?.properties?.timeseries?.get(0)?.data
        val todaysWeather = TodaysWeatherData(
            tempNow = nowcastData?.instant?.details?.airTemperature.toString(), // nowcast
            tempMax = locationData?.next6Hours?.details?.airTemperatureMax.toString(),
            tempMin = locationData?.next6Hours?.details?.airTemperatureMin.toString(),
            uv = locationData?.next6Hours?.details?.ultravioletIndexClearSky.toString(),
            wind = nowcastData?.instant?.details?.windSpeed.toString(), // nowcast
            precipitationAmount = nowcastData?.instant?.details?.precipitationAmount.toString(), // nowcast
            iconDesc = locationData?.next6Hours?.summary?.symbolCode.toString(),
            iconDescNow = nowcastData?.next1Hours?.summary?.symbolCode.toString() // nowcast
        )
        return todaysWeather
    }

}

suspend fun main() {

    val d = LocationForecastDataSource()
    val loc = LocationForecastRepository(d)
    //val w = WeatherRepository(null, loc, null)
    //val test = w.getFavoriteLocationsData()
    //println("${test[0]} ${test[1]}")
}