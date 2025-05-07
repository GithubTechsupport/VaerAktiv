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
import no.uio.ifi.in2000.vaeraktiv.data.places.PlacesRepository
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.IMetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.data.welcome.PreferenceRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlaceSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.utils.weatherDescriptions
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

class WeatherRepositoryDefault @Inject constructor(
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
) : WeatherRepository {
    private val _currentLocation = MutableLiveData<Location?>()
    override val currentLocation: LiveData<Location?> get() = _currentLocation

    private val _deviceLocation = MutableLiveData<Location?>()
    override val deviceLocation: LiveData<Location?> get() = _deviceLocation

    private val _activities = MutableLiveData<List<SuggestedActivities?>?>(List(8) { null })
    override val activities: LiveData<List<SuggestedActivities?>?> get() = _activities

    override fun setCurrentLocation(location: Location) {
        Log.d("setCurrentLocation", "setCurrentLocation called with location: $location")
        if (location == _currentLocation.value) {
            Log.d("setCurrentLocation", "Location is the same as current location, not updating.")
            return
        }
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
            val forecast = locationForecastRepository.getForecast(latitude, longitude)
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
                    wind = forecastData.instant.details.windSpeed?.toString() ?: "N/A",
                    downPour = forecastData.next6Hours?.details?.precipitationAmount?.toString() ?: "N/A",
                    uv = forecastData.instant.details.ultravioletIndexClearSky?.toString() ?: "N/A",
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    override suspend fun getTimeSeriesForDay(location: Location, dayNr: Int) : Pair<List<TimeSeries>, Units?> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon)
            val fullTimeseries = response.first
            val units = response.second
            val timeseries = fullTimeseries[dayNr].second
            return Pair(timeseries, units)
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error at getTimeSeriesForDay: ", e)
            throw e
        }
    }

    override suspend fun getForecastByDay(location: Location): List<ForecastForDay> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon).first.drop(1)
                .dropLast(1) // liste med TimeSeries for datoen
            val forecast = response.map { (date, timeSeriesList) ->
                val timeSeriesAt12PM = timeSeriesList.find { it.time.substring(11, 16) == "12:00" }
                ForecastForDay(
                    date = date,
                    maxTemp = timeSeriesAt12PM?.data?.next6Hours?.details?.airTemperatureMax.toString(),
                    icon = timeSeriesAt12PM?.data?.next6Hours?.summary?.symbolCode.toString(),
                )
            }
            return forecast
        } catch (e: Exception) {
            Log.e("WeatherRepository", "Error at getForecastByDay: ", e)
            throw e
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForecastByDayIntervals(location: Location): List<List<DetailedForecastForDay>> {
        try {
            val response = locationForecastRepository.getForecastByDay(location.lat, location.lon).first.drop(1).dropLast(1)

            val intervals = listOf("02", "08", "14", "20") // tilsvarende tidspunkter i UTC vil ha varsel for de neste 6 timene 7 dager fremover.
            val osloZone = ZoneId.of("Europe/Oslo")

            val forecastByDay = response.map { (dateStr, timeSeriesList) ->
                val localDate = LocalDate.parse(dateStr)

                intervals.map { localHour ->
                    val localDateTime = ZonedDateTime.of(localDate, LocalTime.of(localHour.toInt(), 0), osloZone)
                    val utcHour = localDateTime.withZoneSameInstant(ZoneId.of("UTC")).hour.toString().padStart(2, '0')
                    val utcDate = localDateTime.withZoneSameInstant(ZoneId.of("UTC")).toLocalDate().toString()

                    val matchPrefix = "${utcDate}T${utcHour}" // Example: 2024-07-01T04
                    val timeSeries = timeSeriesList.find { it.time.startsWith(matchPrefix) }

                    val endHour = (localHour.toInt() + 6) % 24
                    val end = endHour.toString().padStart(2, '0')
                    DetailedForecastForDay(
                        date = dateStr,
                        interval = "$localHour - $end",
                        icon = timeSeries?.data?.next6Hours?.summary?.symbolCode ?: "N/A"
                    )
                }
            }
            return forecastByDay

        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O) // krever API versjon 26
    override suspend fun getForecastForHour(location: Location): List<ForecastForHour> {
        val response = locationForecastRepository.getNext24Hours(location.lat, location.lon)
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
        return locationForecastRepository.getForecast(location.lat, location.lon)
    }

    override suspend fun getSuggestedActivitiesForOneDay(location: Location, dayNr: Int): SuggestedActivities {
        val response = getTimeSeriesForDay(location, dayNr)
        val timeseries = response.first
        val units = response.second
        val places = getNearbyPlaces(location)
        val routes = stravaRepository.getRouteSuggestions(location)
        val preferences = preferenceRepository.getEnabledPreferences()
        if (units == null) {
            throw Exception("Units are null")
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

        if (units == null) throw Exception("Units are null")

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

    override fun resetActivities() {
        _activities.value = List(8) { null }
    }

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
                Log.e("WeatherRepository", "Error getting device location: ", e)
                return@startTracking
            }

            newLocation
                .takeUnless { it == deviceLocation.value }
                ?.also { _deviceLocation.value = it }
            Log.d("DeviceLocation", "New device location: $newLocation")
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
