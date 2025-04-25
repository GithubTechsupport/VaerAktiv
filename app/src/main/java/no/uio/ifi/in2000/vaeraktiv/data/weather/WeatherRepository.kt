package no.uio.ifi.in2000.vaeraktiv.data.weather

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import kotlinx.coroutines.Deferred
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.placesRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay

interface WeatherRepository {

    // LiveData properties exposed for observing current values
    val currentLocation: LiveData<Location?>
    val deviceLocation: LiveData<Location?>

    // Updates the current location value.
    fun setCurrentLocation(location: Location)

    // Fetches sunrise data for the provided location and date.
    suspend fun getSunRiseData(location: Location, date: String): List<String>

    // Fetches weather data for a list of favorite locations.
    suspend fun getFavoriteLocationsData(locationsList: List<String>): MutableList<FavoriteLocation>

    // Fetches weather alerts for a specific location.
    suspend fun getAlertsForLocation(location: Location): MutableList<AlertData>

    // Retrieves today's forecast for the provided location.
    suspend fun getForecastToday(location: Location): ForecastToday

    suspend fun getTimeSeriesForDay(dayNr: Int, location: Location): List<TimeSeries>

    // Retrieves a forecast segmented by day.
    suspend fun getForecastByDay(location: Location): List<ForecastForDay>

    // Retrieves the full weather forecast for the provided location.
    suspend fun getWeatherForecast(location: Location): LocationForecastResponse?

    // Uses AI to get activities based on the weather forecast.
    suspend fun getActivities(location: Location): JsonResponse?

    // Starts tracking the device location, providing updates to observers.
    fun trackDeviceLocation(lifecycleOwner: LifecycleOwner)

    // Retrieves autocomplete predictions based on the given query and session token.
    suspend fun getAutocompletePredictions(
        query: String,
        sessionToken: AutocompleteSessionToken
    ): List<AutocompletePrediction>

    suspend fun getNearbyPlaces(location: Location): NearbyPlacesSuggestions
}
