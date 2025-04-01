package no.uio.ifi.in2000.vaeraktiv.data.weather

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val metAlertsRepository: MetAlertsRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository, //bare nullable for testing
    private val aiRepository: AiRepository,
    private val deviceLocationRepository: LocationRepository,
    private val geocoderClass: GeocoderClass
) {

    private var locations: MutableMap<String, Pair<String, String>> = mutableMapOf()

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _deviceLocation = MutableLiveData<Location?>()
    val deviceLocation: LiveData<Location?> get() = _deviceLocation

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

    suspend fun getWeatherForecast(location: Location): LocationForecastResponse? {
        return locationForecastRepository.getForecast(location.lat.toString(), location.lon.toString())
    }

    suspend fun getActivities(location: Location): JsonResponse? {
        val weatherForecast = getWeatherForecast(location)
        if (weatherForecast == null) {
            throw Exception("Weather forecast is null")
        } else {
            return aiRepository.getResponse(Prompt(weatherForecast.properties, location.addressName))
        }
    }

    fun trackDeviceLocation(lifecycleOwner: LifecycleOwner) {
        deviceLocationRepository.startTracking(lifecycleOwner) {
            deviceLocation.value?.let { devLoc ->
                if (devLoc.lat == it.latitude && devLoc.lon == it.longitude) {
                    Log.d("WeatherRepository", "Device location is already up to date")
                    return@startTracking
                }
            }
            try {
                val lat = String.format("%.3f", it.latitude).toDouble()
                val lon = String.format("%.3f", it.longitude).toDouble()
                val location = geocoderClass.getLocationFromCoordinates(Pair(lat, lon))?.let { address ->
                    Location(address.getAddressLine(0), lat, lon)
                } ?: Location("Unknown location", lat, lon)
                _deviceLocation.postValue(location)
                _currentLocation.postValue(location)
            } catch (e: Exception) {
                Log.d("WeatherRepository", "Error getting device location: ${e.message}")
                throw e
            }
        }
    }
}

//private fun collectLastLocation() {
//    locationRepository.startTracking(this) { location ->
//        //val coordinates = Pair(location.latitude, location.longitude)
//        val coordinates = Pair(59.9522, 10.8874)
//        Log.d("MainActivity", "Coordinates: $coordinates")
//        CoroutineScope(Dispatchers.Main).launch {
//            try {
//                val address = geocoderClass.getLocationFromCoordinates(coordinates)
//                if (address != null) {
//                    Log.d("MainActivity", address.toString())
//                } else {
//                    Log.d("MainActivity", "Address is unknown")
//                }
//            } catch (e: Exception) {
//                Log.d("MainActivity", "Error getting location name: ${e.message}")
//            }
//        }
//    }
//}

suspend fun main() {

    val d = LocationForecastDataSource()
    val loc = LocationForecastRepository(d)
    //val w = WeatherRepository(null, loc, null)
    //val test = w.getFavoriteLocationsData()
    //println("${test[0]} ${test[1]}")
}