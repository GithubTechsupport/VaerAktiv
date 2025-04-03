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
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
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
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import javax.inject.Inject
import no.uio.ifi.in2000.vaeraktiv.utils.weatherDescriptions

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

    fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

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
            val icon = data?.next12Hours?.summary?.symbolCode.toString()
            val key = icon.substringBefore("_")
            val desc = weatherDescriptions[key] ?: "Ukjent vær"
            val weatherData = FavoriteLocation(
                name = placeName,
                iconDesc = icon,
                shortDesc = desc,
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

    suspend fun getForecastToday(location: Location): ForecastToday {
        val forecast = locationForecastRepository.getForecast(location.lat.toString(), location.lon.toString())
        val locationData = forecast?.properties?.timeseries?.get(0)?.data
        val nowcast = nowcastRepository.getForecast(location.lat.toString(), location.lon.toString())
        val nowcastData = nowcast?.properties?.timeseries?.get(0)?.data
        val forecastToday = ForecastToday(
            tempNow = nowcastData?.instant?.details?.airTemperature.toString(), // nowcast
            tempMax = locationData?.next6Hours?.details?.airTemperatureMax.toString(),
            tempMin = locationData?.next6Hours?.details?.airTemperatureMin.toString(),
            uv = locationData?.instant?.details?.ultravioletIndexClearSky.toString(),
            windSpeed = nowcastData?.instant?.details?.windSpeed.toString(), // nowcast
            precipitationAmount = nowcastData?.next1Hours?.details?.precipitationAmount.toString(), // nowcast
            icon = locationData?.next6Hours?.summary?.symbolCode.toString(),
            iconNow = nowcastData?.next1Hours?.summary?.symbolCode.toString() // nowcast
        )
        return forecastToday
    }

    suspend fun getForecastByDay(location: Location): List<ForecastForDay> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat.toString(), location.lon.toString()) // liste med TimeSeries for datoen
            if (response != null) {
                val forecast = response.map { (date, timeSeriesList) ->
                    ForecastForDay(
                        date = date,
                        maxTemp = timeSeriesList[0].data.next6Hours?.details?.airTemperatureMax.toString(),
                        icon = timeSeriesList[0].data.next6Hours?.summary?.symbolCode.toString(),
                    )
                }
                return forecast
            } else {
                Log.d("WeatherRepository", "No forecast found")
                throw Error("No forecast found")
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error at getForecastByDay: ", e)
            throw e
        }


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
                _deviceLocation.value = location
                //Log.d("WeatherRepository", "Device location updated: $location")
            } catch (e: Exception) {
                Log.e("WeatherRepository", "Error getting device location: ", e)
                throw e
            }
        }
    }
}