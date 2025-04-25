package no.uio.ifi.in2000.vaeraktiv.data.weather

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.placesRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.IMetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import javax.inject.Inject
import no.uio.ifi.in2000.vaeraktiv.utils.weatherDescriptions

class WeatherRepositoryDefault @Inject constructor(
    private val metAlertsRepository: IMetAlertsRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository,
    private val favoriteLocationRepository: FavoriteLocationRepository,
    private val aiRepository: AiRepository,
    private val deviceLocationRepository: LocationRepository,
    private val geocoderClass: GeocoderClass,
    private val nowcastRepository: NowcastRepository,
    private val placesRepository: placesRepository
) : WeatherRepository {

    private var locations: MutableMap<String, Pair<String, String>> = mutableMapOf()

    private val _currentLocation = MutableLiveData<Location?>()
    override val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _deviceLocation = MutableLiveData<Location?>()
    override val deviceLocation: LiveData<Location?> get() = _deviceLocation

    override fun setCurrentLocation(location: Location) {
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getSunRiseData(location: Location, date : String): List<String> {
        Log.d("WeatherRepository", "getSunRiseData called with location: $location and date: $date")
        val response = sunriseRepository.getSunriseTime(location.lat, location.lon, date)
        Log.d("WeatherRepository", "getSunRiseData response: $response")
        return response.map { timestring ->
            val utcTime = java.time.OffsetDateTime.parse(timestring)
            val cetTime = utcTime.plusHours(1)
            cetTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        }
    }


    override suspend fun getFavoriteLocationsData(locationsList: List<String>): MutableList<FavoriteLocation> {
        val locationsData:MutableList<FavoriteLocation> = mutableListOf()
        locationsList.forEach { line ->
            val parts = line.split(",")
            val placeName = parts[0]
            val lat = parts[1]
            val lon = parts[2]
            val forecast = locationForecastRepository.getForecast(lat, lon)
            val data = forecast?.properties?.timeseries?.get(0)?.data
            val icon = data?.next6Hours?.summary?.symbolCode.toString() //bruk nowcast
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
                uv = data?.instant?.details?.ultravioletIndexClearSky.toString(),
                lat = lat,
                lon = lon,
            )
            locationsData.add(weatherData)

        }
        return locationsData
    }

    override suspend fun getAlertsForLocation(location: Location): MutableList<AlertData> {
        val alertDataList:MutableList<AlertData> = mutableListOf()

        val response = metAlertsRepository.getAlertsForLocation(location.lat,
            location.lon)
        response.forEach { feature ->
            val alert = AlertData(
                area = feature.properties.area.toString(),
                awareness_type = feature.properties.awareness_type.toString(),
                description = feature.properties.description.toString(),
                eventAwarenessName = feature.properties.eventAwarenessName.toString(),
                instruction = feature.properties.instruction.toString(),
                riskMatrixColor = feature.properties.riskMatrixColor.toString(),
                contact = feature.properties.contact.toString()
            )
            alertDataList.add(alert)
        }
        return alertDataList
    }

    override suspend fun getForecastToday(location: Location): ForecastToday {
        getForecastForHour(location)
        val forecast = locationForecastRepository.getForecast(location.lat, location.lon)
        val locationData = forecast?.properties?.timeseries?.get(0)?.data
        val nowcast = nowcastRepository.getForecast(location.lat, location.lon)
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

    override suspend fun getTimeSeriesForDay(dayNr: Int, location: Location) : List<TimeSeries> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon)
            if (response != null) {
                val timeSeries = response.get(dayNr).second
                return timeSeries
            } else {
                Log.d("WeatherRepository", "No forecast found")
                throw Error("No forecast found")
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error at getTimeSeriesForDay: ", e)
            throw e
        }
    }

    override suspend fun getForecastByDay(location: Location): List<ForecastForDay> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon) // liste med TimeSeries for datoen
            if (response != null) {
                val forecast = response.map { (date, timeSeriesList) ->
                    val timeSeriesAt12PM = timeSeriesList.find { it.time.substring(11, 16) == "12:00" }
                    ForecastForDay(
                        date = date,
                        maxTemp = timeSeriesAt12PM?.data?.next6Hours?.details?.airTemperatureMax.toString(),
                        icon = timeSeriesAt12PM?.data?.next6Hours?.summary?.symbolCode.toString(),
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

    override suspend fun getForecastForHour(location: Location): List<ForecastForHour> {
        val response = locationForecastRepository.getNext24Hours(location.lat, location.lon)
        Log.d("WeatherRepository", "getWeatherForHour response: $response")
        val hourDataList:List<ForecastForHour> = response?.map { timeSeries ->
            val forecastForHour = ForecastForHour(
                temp = timeSeries.data.instant.details.airTemperature.toString(),
                windSpeed = timeSeries.data.instant.details.windSpeed.toString(),
                precipitationAmount = timeSeries.data.next1Hours?.details?.precipitationAmount.toString(),
                icon = timeSeries.data.next1Hours?.summary?.symbolCode.toString())
            forecastForHour
        } ?: emptyList()
        Log.d("WeatherRepository", "getWeatherForHour response: $hourDataList")
        return hourDataList
    }

    override suspend fun getWeatherForecast(location: Location): LocationForecastResponse? {
        return locationForecastRepository.getForecast(location.lat, location.lon)
    }

    override suspend fun getActivities(location: Location): JsonResponse? {
        val weatherForecast = getWeatherForecast(location)
        if (weatherForecast == null) {
            throw Exception("Weather forecast is null")
        } else {
            return aiRepository.getResponse(Prompt(weatherForecast.properties, location.addressName))
        }
    }

    @SuppressLint("DefaultLocale")
    override fun trackDeviceLocation(lifecycleOwner: LifecycleOwner) {
        deviceLocationRepository.startTracking(lifecycleOwner) {
            deviceLocation.value?.let { devLoc ->
                if (devLoc.lat == it.latitude.toString() && devLoc.lon == it.longitude.toString()) {
                    Log.d("WeatherRepository", "Device location is already up to date")
                    return@startTracking
                }
            }
            try {
                val lat = String.format("%.3f", it.latitude)
                val lon = String.format("%.3f", it.longitude)
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

    override suspend fun getAutocompletePredictions(query: String, sessionToken: AutocompleteSessionToken): List<AutocompletePrediction> {
        return placesRepository.getAutocompletePredictions(query, sessionToken)
    }
}
