package no.uio.ifi.in2000.vaeraktiv.data.weather

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.PlacesRepository
import no.uio.ifi.in2000.vaeraktiv.data.preferences.PreferenceRepository
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.IMetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlaceSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.home.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units
import no.uio.ifi.in2000.vaeraktiv.utils.isInNorway
import no.uio.ifi.in2000.vaeraktiv.utils.weatherDescriptions
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

/**
 * Aggregates data repositories for external and internal APIs to combine and format data.
 *
 * @constructor Injects all required repositories for data composition.
 */
class AggregateRepository @Inject constructor(
    private val metAlertsRepository: IMetAlertsRepository,
    private val locationForecastRepository: LocationForecastRepository,
    private val sunriseRepository: SunriseRepository,
    private val aiRepository: AiRepository,
    private val deviceLocationRepository: LocationRepository,
    private val geocoderClass: GeocoderClass,
    private val nowcastRepository: NowcastRepository,
    private val placesRepository: PlacesRepository,
    private val stravaRepository: StravaRepository,
    private val preferenceRepository: PreferenceRepository
) : IAggregateRepository {
    private val _currentLocation = MutableLiveData<Location?>()
    override val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _deviceLocation = MutableLiveData<Location?>()
    override val deviceLocation: LiveData<Location?> get() = _deviceLocation

    private val _activities = MutableLiveData<List<SuggestedActivities?>>(List(8) { null })
    override val activities: LiveData<List<SuggestedActivities?>> get() = _activities

    /**
     * Sets current location to be displayed on homescreen.
     * Filters out locations outside Norway.
     * Does not assign location if it is the same as the current one.
     *
     * @param location new location to apply.
     */
    override fun setCurrentLocation(location: Location) {
        Log.d("setCurrentLocation", "called with $location")

        val lat = location.lat.toDoubleOrNull()
        val lon = location.lon.toDoubleOrNull()
        if (lat == null || lon == null || !isInNorway(lat, lon)) {
            Log.d("setCurrentLocation", "Ignored outside Norway: $location")
            return
        }

        if (location == _currentLocation.value) {
            Log.d("setCurrentLocation", "No change, not updating.")
            return
        }

        _currentLocation.value = location
    }

    /**
     * Retrieves formatted sunrise/sunset times for a date.
     *
     * @param location location coordinates
     * @param date ISO date string (yyyy-MM-dd)
     * @return list of local time strings "HH:mm"
     */
    override suspend fun getSunRiseData(location: Location, date: String): List<String> {
        Log.d("AggregateRepository", "getSunRiseData called with location: $location and date: $date")
        val response = sunriseRepository.getSunriseTime(location.lat, location.lon, date)
        Log.d("AggregateRepository", "getSunRiseData response: $response")
        return response.map { timestring ->
            val utcTime = java.time.OffsetDateTime.parse(timestring)
            val cetTime = utcTime.plusHours(1)
            cetTime.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        }
    }

    /**
     * Retrieves favorite locations stored in JSON files.
     *
     * @param locationsList list of "name,lat,lon" strings
     * @return list of FavoriteLocation or empty on parse errors
     */
    override suspend fun getFavoriteLocationsData(locationsList: List<String>): List<FavoriteLocation> {
        return locationsList.mapNotNull { line ->
            parseLocationLine(line)?.let { (placeName, latitude, longitude) ->
                fetchAndProcessForecast(placeName, latitude, longitude)
            }
        }.also {
            Log.d("AggregateRepository", "All favorite locations data processed")
        }
    }

    /** Parses "name,lat,lon" line into a Triple or null if malformed. */
    private fun parseLocationLine(line: String): Triple<String, String, String>? {
        val parts = line.split(",")
        if (parts.size != 3) {
            Log.e("AggregateRepository", "Invalid location format: $line")
            return null
        }
        return Triple(parts[0], parts[1], parts[2])
    }

    /**
     * Fetches forecast and maps it to a FavoriteLocation or null on error.
     */
    private suspend fun fetchAndProcessForecast(placeName: String, latitude: String, longitude: String): FavoriteLocation? {
        return try {
            val forecast = locationForecastRepository.getForecast(latitude, longitude)
            val data = forecast?.properties?.timeseries?.get(0)?.data

            data?.let { forecastData ->
                val iconCode = forecastData.next6Hours?.summary?.symbolCode // the code for the icon doubles as an English weather description
                val iconKey = iconCode?.substringBefore("_") // ignores the _night or _daytime part of the code
                val description = weatherDescriptions[iconKey] // makes the English descriptions Norwegian

                FavoriteLocation(
                    name = placeName,
                    iconDesc = iconCode,
                    shortDesc = description,
                    highestTemp = forecastData.next6Hours?.details?.airTemperatureMax?.toString(),
                    lowestTemp = forecastData.next6Hours?.details?.airTemperatureMin?.toString(),
                    wind = forecastData.instant.details.windSpeed?.toString(),
                    downPour = forecastData.next6Hours?.details?.precipitationAmount?.toString(),
                    uv = forecastData.instant.details.ultravioletIndexClearSky?.toString(),
                    lat = latitude,
                    lon = longitude
                ).also { Log.d("AggregateRepository", "Favorite location data: $it") }
            } ?: run {
                Log.w("AggregateRepository", "No forecast data available for $placeName")
                null
            }
        } catch (e: Exception) {
            Log.e("AggregateRepository", "Error fetching forecast for $placeName", e)
            null
        }
    }

    /**
     * Fetches active alerts for the given location.
     *
     * @param location coordinates to query
     * @return list of AlertData
     */
    override suspend fun getAlertsForLocation(location: Location): MutableList<AlertData> {
        val alertDataList: MutableList<AlertData> = mutableListOf()

        val response = metAlertsRepository.getAlertsForLocation(location.lat, location.lon)
        response.forEach { feature ->
            val alert = AlertData(
                area = feature.properties.area.toString(),
                awarenessType = feature.properties.awarenessType.toString(),
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

    /**
     * Uses nowcast to get weather forecast today.
     * Uses location forecast for min and max temp, uv, and icon.
     *
     * @param location coordinates for weather lookup
     * @return ForecastToday object
     */
    override suspend fun getForecastToday(location: Location): ForecastToday {
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

    /**
     * Returns time series and units for a specific day; throws on failure.
     */
    override suspend fun getTimeSeriesForDay(location: Location, dayNr: Int): Pair<List<TimeSeries>, Units?> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon)
            val fullTimeseries = response.first
            val units = response.second
            val timeseries = fullTimeseries[dayNr].second // get only the time series for the specified day.
            return Pair(timeseries, units)
        } catch (e: Exception) {
            Log.e("AggregateRepository", "Error at getTimeSeriesForDay: ", e)
            throw e
        }
    }

    /**
     * Extracts daily forecasts at 12:00 for upcoming days.
     */
    override suspend fun getForecastByDay(location: Location): List<ForecastForDay> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon).first.drop(1)
                .dropLast(1) // list of time series for each day
            val forecast = response.map { (date, timeSeriesList) ->
                // finds the forecast at 12:00 for the coming days
                val timeSeriesAt12PM = timeSeriesList.find { it.time.substring(11, 16) == "12:00" }
                ForecastForDay(
                    date = date,
                    maxTemp = timeSeriesAt12PM?.data?.next6Hours?.details?.airTemperatureMax.toString(),
                    icon = timeSeriesAt12PM?.data?.next6Hours?.summary?.symbolCode.toString(),
                )
            }
            return forecast
        } catch (e: Exception) {
            Log.e("AggregateRepository", "Error at getForecastByDay: ", e)
            throw e
        }
    }

    /**
     * Returns next 24 h hourly forecast in local time.
     *
     * @param location coordinates to query
     * @return list of ForecastForHour entries
     */
    override suspend fun getForecastByDayIntervals(location: Location): List<List<DetailedForecastForDay>> {
        try {
            val response = locationForecastRepository
                .getForecastByDay(location.lat, location.lon)
                .first
                .drop(1)
                .dropLast(1)

            val utcIntervals = listOf("00", "06", "12", "18")
            val osloZone = ZoneId.of("Europe/Oslo")
            val utcZone = ZoneId.of("UTC")

            val forecastByDay = response.map { (dateStr, timeSeriesList) ->
                val utcDate = LocalDate.parse(dateStr)
                // the API goes by UTC time and our app uses Norwegian (CET/CEST) time so the following code fixes the intervals.
                utcIntervals.map { utcHourStr ->
                    val utcHour = utcHourStr.toInt()

                    val matchPrefix = "${dateStr}T$utcHourStr"
                    val timeSeries = timeSeriesList.find { it.time.startsWith(matchPrefix) }

                    val startUtcDateTime = ZonedDateTime.of(utcDate, LocalTime.of(utcHour, 0), utcZone)
                    val endUtcDateTime = startUtcDateTime.plusHours(6)

                    val localStart = startUtcDateTime.withZoneSameInstant(osloZone)
                    val localEnd = endUtcDateTime.withZoneSameInstant(osloZone)

                    // The interval displayed to the user.
                    val intervalDisplay = "${localStart.hour.toString().padStart(2, '0')} - ${localEnd.hour.toString().padStart(2, '0')}"

                    DetailedForecastForDay(
                        date = dateStr,
                        interval = intervalDisplay,
                        icon = timeSeries?.data?.next6Hours?.summary?.symbolCode
                    )
                }
            }

            Log.d("AggregateRepository", "forecastByDay: $forecastByDay")
            return forecastByDay

        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    /**
     * Retrieves next 24-hour hourly forecasts with local times.
     */
    override suspend fun getForecastForHour(location: Location): List<ForecastForHour> {
        val response = locationForecastRepository.getNext24Hours(location.lat, location.lon)
        Log.d("AggregateRepository", "getWeatherForHour response: $response")
        val hourDataList: List<ForecastForHour> = response?.map { timeSeries ->
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
        Log.d("AggregateRepository", "getWeatherForHour response: $hourDataList")
        return hourDataList
    }

    /**
     * Retrieves location forecast response.
     *
     * @param location coordinates to query
     * @return LocationForecastResponse
     */
    override suspend fun getWeatherForecast(location: Location): LocationForecastResponse? {
        return locationForecastRepository.getForecast(location.lat, location.lon)
    }

    /**
     * Gets AI-suggested activities for a full day.
     * 3 activities for different intervals.
     *
     * @param location where to suggest
     * @param dayNr zero-based day index
     * @return SuggestedActivities for that day
     */
    override suspend fun getSuggestedActivitiesForOneDay(location: Location, dayNr: Int): SuggestedActivities {
        val response = getTimeSeriesForDay(location, dayNr)
        val timeseries = response.first
        val units = response.second
        val places = getNearbyPlaces(location)
        val routes = stravaRepository.getRouteSuggestions(location)
        val preferences = preferenceRepository.getEnabledPreferences()
        val excludedActivities = activities.value
            ?.flatMap { day -> day?.activities.orEmpty() }
            ?.joinToString("\n") { "${it.activityName} (${it.timeStart} - ${it.timeEnd}): ${it.activityDesc}" }
            .orEmpty()
        val exclusionString = "EXCLUDE THE FOLLOWING ACTIVITIES:\n\n$excludedActivities\n\n" // Format the prompt for the AI
        if (units == null) {
            throw Exception("Units are null")
        }
        return aiRepository.getSuggestionsForOneDay(
            FormattedForecastDataForPrompt(timeseries, units, location.addressName),
            places, routes, preferences, exclusionString
        )
    }

    /**
     * Gets a single AI activity suggestion avoiding overlaps.
     *
     * @param location where to suggest
     * @param dayNr zero-based day index
     * @param index position in existing list
     * @return a new ActivitySuggestion
     */
    override suspend fun getSuggestedActivity(location: Location, dayNr: Int, index: Int): ActivitySuggestion {
        val response = getTimeSeriesForDay(location, dayNr)
        val timeseries = response.first
        val units = response.second

        val places = getNearbyPlaces(location)

        val routes = stravaRepository.getRouteSuggestions(location)

        val preferences = preferenceRepository.getEnabledPreferences()

        // The next suggested activities should not be at the same time as the previously suggested ones.
        val previousInterval = activities.value?.get(dayNr)?.activities?.getOrNull(index - 1)?.timeEnd
        val previousIntervalString = previousInterval?.let { "\n\nSUGGESTED ACTIVITY NEEDS TO BE AFTER $it" } ?: ""
        val excludedActivities = activities.value
            ?.flatMap { day -> day?.activities.orEmpty() }
            ?.joinToString("\n") { "${it.activityName} (${it.timeStart} - ${it.timeEnd}): ${it.activityDesc}" }
            .orEmpty()
        val exclusionString = "EXCLUDE THE FOLLOWING ACTIVITIES:\n\n$excludedActivities$previousIntervalString\n\n"

        if (units == null) throw Exception("Units are null")

        val suggestions = aiRepository.getSingleSuggestionForDay(
            FormattedForecastDataForPrompt(timeseries, units, location.addressName),
            places, routes, preferences, exclusionString
        )

        return suggestions
    }

    /**
     * Replaces cached activities for a specified day with new ones.
     *
     * @param dayNr zero-based day index
     * @param newActivities new SuggestedActivities list
     */
    override fun replaceActivitiesForDay(dayNr: Int, newActivities: SuggestedActivities) {
        val current = _activities.value ?: return
        val updated = current.toMutableList().apply {
            this[dayNr] = newActivities
        }
        _activities.value = updated
    }

    /**
     * Replaces one cached activity in a day.
     *
     * @param dayNr zero-based day index
     * @param index activity position
     * @param newActivity replacement ActivitySuggestion
     */
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

    /**
     * Resets all cached AI activity suggestions.
     */
    override fun resetActivities() {
        _activities.value = List(8) { null }
    }

    /**
     * Starts device location tracking with lifecycle awareness.
     *
     * @param lifecycleOwner host lifecycle for callbacks
     */
    @SuppressLint("DefaultLocale")
    override fun trackDeviceLocation(lifecycleOwner: LifecycleOwner) {
        deviceLocationRepository.startTracking(lifecycleOwner) { location ->
            val lat = String.format("%.3f", location.latitude)
            val lon = String.format("%.3f", location.longitude)

            val newLocation = try {
                val addressLine = geocoderClass
                    .getLocationFromCoordinates(Pair(lat, lon))
                    ?.getAddressLine(0)
                Location(addressLine ?: "Unknown location", lat, lon)
            } catch (e: Exception) {
                Log.e("AggregateRepository", "Error getting device location: ", e)
                return@startTracking
            }

            newLocation // if the current location changed then track the new one
                .takeUnless { it == deviceLocation.value }
                ?.also { _deviceLocation.value = it }
            Log.d("DeviceLocation", "New device location: $newLocation")
        }
    }

    /**
     * Retrieves place autocomplete suggestions.
     *
     * @param query user input string
     * @param sessionToken Google Places session token
     * @return list of AutocompletePrediction
     */
    override suspend fun getAutocompletePredictions(query: String, sessionToken: AutocompleteSessionToken): List<AutocompletePrediction> {
        return placesRepository.getAutocompletePredictions(query, sessionToken)
    }

    /**
     * Fetches nearby place suggestions.
     *
     * @param location coordinates to search around
     * @return NearbyPlacesSuggestions model
     */
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
