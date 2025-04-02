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
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Features
import no.uio.ifi.in2000.vaeraktiv.model.ui.ThisWeeksWeatherData
import no.uio.ifi.in2000.vaeraktiv.model.ui.TodaysWeatherData
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val metAlertsRepository: MetAlertsRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository, //bare nullable for testing
    private val favoriteLocationRepository: FavoriteLocationRepository,
    private val aiRepository: AiRepository,
    private val deviceLocationRepository: LocationRepository,
    private val geocoderClass: GeocoderClass,
    private val nowcastRepository: NowcastRepository
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

    suspend fun getAlertsForLocation(location: Location): MutableList<AlertData> {
        val alertDataList:MutableList<AlertData> = mutableListOf()

        val response = metAlertsRepository.getAlertsForLocation(location.lat.toString(),
            location.lon.toString())
        response.forEach { feature ->
            val alert = AlertData(
                area = feature.properties.area.toString(),
                awareness_type = feature.properties.awareness_type.toString(),
                description = feature.properties.description.toString(),
                eventAwarenessName = feature.properties.eventAwarenessName.toString(),
                instruction = feature.properties.instruction.toString(),
                riskMatrixColor = feature.properties.riskMatrixColor.toString()
            )
            alertDataList.add(alert)
        }
        return alertDataList
    }

    /*
        suspend fun getAlertsForLocation(location: Location): List<AlertData> {
        val response = metAlertsRepository.getAlertForLocation(location.lat.toString(),
            location.lon.toString())
        val alert = AlertData(
            area = response?.properties?.area.toString(),
            awareness_type = response?.properties?.awareness_type.toString(),
            description = response?.properties?.description.toString(),
            eventAwarenessName = response?.properties?.eventAwarenessName.toString(),
            instruction = response?.properties?.instruction.toString(),
            riskMatrixColor = response?.properties?.riskMatrixColor.toString()
        )
        return alert
    }
     */

    suspend fun getTodaysData(location: Location): TodaysWeatherData {
        val forecast = locationForecastRepository.getForecast(location.lat.toString(), location.lon.toString())
        val locationData = forecast?.properties?.timeseries?.get(0)?.data
        val nowcast = nowcastRepository.getForecast(location.lat.toString(), location.lon.toString())
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

    suspend fun getDayInWeekData(location: Location, date: String): ThisWeeksWeatherData {
        val forecast = locationForecastRepository.getForecastForDate(location.lat.toString(),
            location.lon.toString(), date) // liste med TimeSeries for datoen
        val locationData = forecast?.get(0)?.data // tror dette skal være data fra 00:00 for valgt dato
        val thisWeeksWeather = ThisWeeksWeatherData(
            date = date,
            maxTemp = locationData?.next12Hours?.details?.airTemperatureMax.toString(), // burde finne maxtemp mellom 00:00 - 23:59 for en dag (neste 24t)
            uvMax = locationData?.next6Hours?.details?.ultravioletIndexClearSky.toString(), // brukes ikke
            iconDesc = locationData?.next12Hours?.summary?.symbolCode.toString(), // kanskje helt ikke optimalt
            precipitationAmount = locationData?.next6Hours?.details?.precipitationAmount.toString(), // brukes ikke
            wind = locationData?.instant?.details?.windSpeed.toString() // brukes ikke
        )
        return thisWeeksWeather
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

suspend fun main() {

    val d = LocationForecastDataSource()
    val loc = LocationForecastRepository(d)
    //val w = WeatherRepository(null, loc, null)
    //val test = w.getFavoriteLocationsData()
    //println("${test[0]} ${test[1]}")
}