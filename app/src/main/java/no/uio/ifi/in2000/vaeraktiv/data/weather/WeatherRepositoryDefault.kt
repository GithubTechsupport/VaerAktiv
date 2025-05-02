package no.uio.ifi.in2000.vaeraktiv.data.weather

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.placesRepository
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.IMetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.data.welcome.PreferenceRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlaceSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import javax.inject.Inject
import no.uio.ifi.in2000.vaeraktiv.utils.weatherDescriptions
import java.time.ZoneId
import java.time.ZonedDateTime

class WeatherRepositoryDefault @Inject constructor(
    private val metAlertsRepository: IMetAlertsRepository,
    private val LocationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository,
    private val favoriteLocationRepository: FavoriteLocationRepository,
    private val aiRepository: AiRepository,
    private val deviceLocationRepository: LocationRepository,
    private val geocoderClass: GeocoderClass,
    private val nowcastRepository: NowcastRepository,
    private val placesRepository: placesRepository,
    private val stravaRepository: StravaRepository,
    private val preferenceRepository: PreferenceRepository
) : WeatherRepository {

    private var locations: MutableMap<String, Pair<String, String>> = mutableMapOf()

    private val _currentLocation = MutableLiveData<Location?>()
    override val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _deviceLocation = MutableLiveData<Location?>()
    override val deviceLocation: LiveData<Location?> get() = _deviceLocation

    private val _activities = MutableLiveData<List<SuggestedActivities?>?>(List(8) { null })
    override val activities: LiveData<List<SuggestedActivities?>?> get() = _activities

    override fun setCurrentLocation(location: Location) {
        _currentLocation.value = location
    }

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


    override suspend fun getFavoriteLocationsData(locationsList: List<String>): List<FavoriteLocation> {
        return locationsList.mapNotNull { line ->
            parseLocationLine(line)?.let { (placeName, latitude, longitude) ->
                fetchAndProcessForecast(placeName, latitude, longitude)
            }
        }.also {
            Log.d("WeatherRepository", "All favorite locations data processed")
        }
    }

    private fun parseLocationLine(line: String): Triple<String, String, String>? {
        val parts = line.split(",")
        if (parts.size != 3) {
            Log.e("WeatherRepository", "Invalid location format: $line")
            return null
        }
        return Triple(parts[0], parts[1], parts[2])
    }

    private suspend fun fetchAndProcessForecast(placeName: String, latitude: String, longitude: String): FavoriteLocation? {
        return try {
            val forecast = LocationForecastRepository.getForecast(latitude, longitude)
            val data = forecast?.properties?.timeseries?.get(0)?.data

            data?.let { forecastData ->
                val iconCode = forecastData.next6Hours?.summary?.symbolCode ?: "unknown"
                val iconKey = iconCode.substringBefore("_")
                val description = weatherDescriptions[iconKey] ?: "Ukjent vær"

                FavoriteLocation(
                    name = placeName,
                    iconDesc = iconCode,
                    shortDesc = description,
                    highestTemp = forecastData.next6Hours?.details?.airTemperatureMax?.toString() ?: "N/A",
                    lowestTemp = forecastData.next6Hours?.details?.airTemperatureMin?.toString() ?: "N/A",
                    wind = forecastData.instant?.details?.windSpeed?.toString() ?: "N/A",
                    downPour = forecastData.next6Hours?.details?.precipitationAmount?.toString() ?: "N/A",
                    uv = forecastData.instant?.details?.ultravioletIndexClearSky?.toString() ?: "N/A",
                    lat = latitude,
                    lon = longitude
                ).also { Log.d("WeatherRepository", "Favorite location data: $it") }
            } ?: run {
                Log.w("WeatherRepository", "No forecast data available for $placeName")
                null
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error fetching forecast for $placeName", e)
            null
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForecastToday(location: Location): ForecastToday {
        val forecast = LocationForecastRepository.getForecast(location.lat, location.lon)
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

    override suspend fun getTimeSeriesForDay(location: Location, dayNr: Int) : Pair<List<TimeSeries>, Units?> {
        try {
            val response = LocationForecastRepository.getForecastByDay(location.lat, location.lon)
            val fullTimeseries = response.first
            val units = response.second
            if (fullTimeseries != null) {
                val timeseries = fullTimeseries.get(dayNr).second
                return Pair(timeseries, units)
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
            val response = LocationForecastRepository.getForecastByDay(location.lat, location.lon).first?.drop(1)?.dropLast(1) // liste med TimeSeries for datoen
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

    // TODO: Fix timezone bug
    override suspend fun getForecastByDayIntervals(location: Location): List<List<DetailedForecastForDay>> {
        try {
            val response = LocationForecastRepository.getForecastByDay(location.lat, location.lon).first?.drop(1)?.dropLast(1) // liste med TimeSeries for datoen
            val intervals = listOf("00", "06", "12", "18")

            if (response != null) {
                val forecastByDay = response.map { (date, timeSeriesList) ->
                    intervals.map { intervalStart ->
                        val timeSeries = timeSeriesList.find { it.time.substring(11, 13) == intervalStart }
                        val end = (intervalStart.toInt() + 6).toString().padStart(2, '0')
                        DetailedForecastForDay(
                            date = date,
                            interval = "$intervalStart - $end",
                            icon = timeSeries?.data?.next6Hours?.summary?.symbolCode ?: "N/A"
                        )
                    }
                }

                return forecastByDay
            } else {
                Log.d("WeatherRepository", "No forecast found")
                throw Error("No forecast found")
            }
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error at getForecastByDayIntervals: ", e)
            throw e
        }
    }


    @RequiresApi(Build.VERSION_CODES.O) // krever API versjon 26
    override suspend fun getForecastForHour(location: Location): List<ForecastForHour> {
        val response = LocationForecastRepository.getNext24Hours(location.lat, location.lon)
        Log.d("WeatherRepository", "getWeatherForHour response: $response")
        val hourDataList:List<ForecastForHour> = response?.map { timeSeries ->
            val forecastForHour = ForecastForHour(
                time = ZonedDateTime.parse(timeSeries.time).withZoneSameInstant(ZoneId.of("Europe/Oslo")).toString().substring(11, 13),
                temp = timeSeries.data.instant.details.airTemperature.toString(),
                windSpeed = timeSeries.data.instant.details.windSpeed.toString(),
                precipitationAmount = timeSeries.data.next1Hours?.details?.precipitationAmount.toString(),
                icon = timeSeries.data.next1Hours?.summary?.symbolCode.toString(),
                uv = timeSeries.data.instant.details.ultravioletIndexClearSky.toString()
            )
            forecastForHour
        } ?: emptyList()
        Log.d("WeatherRepository", "getWeatherForHour response: $hourDataList")
        return hourDataList
    }

    override suspend fun getWeatherForecast(location: Location): LocationForecastResponse? {
        return LocationForecastRepository.getForecast(location.lat, location.lon)
    }

    override suspend fun getSuggestedActivitiesForOneDay(location: Location, dayNr: Int): SuggestedActivities {
        val response = getTimeSeriesForDay(location, dayNr)
        val timeseries = response.first
        val units = response.second
        val places = getNearbyPlaces(location)
        val routes = stravaRepository.getRouteSuggestions(location)
        val preferences = preferenceRepository.getEnabledPreferences()
        if (timeseries == null) {
            throw Exception("Weather forecast is null")
        }
        if (units == null) {
            throw Exception("Units are null")
        }
        if (places == null) {
            throw Exception("Places are null")
        }
        return aiRepository.getSuggestionsForOneDay(
            FormattedForecastDataForPrompt(timeseries, units, location.addressName),
            places, routes, preferences)
    }

    override suspend fun getSuggestedActivity(location: Location, dayNr: Int, index: Int): ActivitySuggestion {
        val startTime = System.currentTimeMillis()

        val responseStart = System.currentTimeMillis()
        val response = getTimeSeriesForDay(location, dayNr)
        val responseEnd = System.currentTimeMillis()
        Log.d("Timing", "getTimeSeriesForDay: ${responseEnd - responseStart} ms")

        val timeseries = response.first
        val units = response.second

        val placesStart = System.currentTimeMillis()
        val places = getNearbyPlaces(location)
        val placesEnd = System.currentTimeMillis()
        Log.d("Timing", "getNearbyPlaces: ${placesEnd - placesStart} ms")

        val routesStart = System.currentTimeMillis()
        val routes = stravaRepository.getRouteSuggestions(location)
        val routesEnd = System.currentTimeMillis()
        Log.d("Timing", "getRouteSuggestions: ${routesEnd - routesStart} ms")

        val preferences = preferenceRepository.getEnabledPreferences()

        val previousInterval = activities.value?.get(dayNr)?.activities?.getOrNull(index - 1)?.timeEnd
        val previousIntervalString = previousInterval?.let { "\n\nSUGGESTED ACTIVITY NEEDS TO BE AFTER $it" } ?: ""
        val excludedActivities = activities.value
            ?.flatMap { day -> day?.activities.orEmpty() }
            ?.joinToString("\n") { "${it.activityName} (${it.timeStart} - ${it.timeEnd}): ${it.activityDesc}" }
            .orEmpty()
        val exclusionString = "EXCLUDE THE FOLLOWING ACTIVITIES:\n\n$excludedActivities$previousIntervalString\n\n"

        if (timeseries == null) throw Exception("Weather forecast is null")
        if (units == null) throw Exception("Units are null")
        if (places == null) throw Exception("Places are null")

        val suggestions = aiRepository.getSingleSuggestionForDay(
            FormattedForecastDataForPrompt(timeseries, units, location.addressName),
            places, routes, preferences, exclusionString
        )

        val endTime = System.currentTimeMillis()
        Log.d("Timing", "Total duration: ${endTime - startTime} ms")

        return suggestions
    }

    override fun replaceActivitiesForDay(dayNr: Int, newActivities: SuggestedActivities) {
        val current = _activities.value ?: return
        val updated = current.toMutableList().apply {
            this[dayNr] = newActivities
        }
        _activities.value = updated
    }

    override fun replaceActivityInDay(dayNr: Int, index: Int, newActivity: ActivitySuggestion) {
        val current = _activities.value ?: return
        val activitiesAtDay = current[dayNr] ?: return
        val newList = activitiesAtDay.activities.toMutableList().apply {
            this[index] = newActivity
        }
        val updatedDay = activitiesAtDay.copy(activities = newList)

        val updated = current.toMutableList().apply {
            this[dayNr] = updatedDay
        }
        _activities.value = updated
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

    override suspend fun getNearbyPlaces(location: Location): NearbyPlacesSuggestions {
        val response = placesRepository.getNearbyPlaces(LatLng(location.lat.toDouble(), location.lon.toDouble()))
        return NearbyPlacesSuggestions(response.map { place ->
            NearbyPlaceSuggestion(
                id = place.id,
                placeName = place.displayName,
                formattedAddress = place.formattedAddress,
                coordinates = place.location?.let { Pair(it.latitude, it.longitude) },
                primaryType = place.primaryType,
                primaryTypeDisplayName = place.primaryTypeDisplayName,
                types = place.placeTypes
            )
        })
    }
}
