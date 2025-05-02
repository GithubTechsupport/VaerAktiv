package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val deviceDateTimeRepository: DeviceDateTimeRepository
) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = weatherRepository.currentLocation

    val activities: LiveData<List<SuggestedActivities?>?> = weatherRepository.activities

    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    private val _navigateToMap = MutableSharedFlow<ActivitySuggestion>()
    val navigateToMap = _navigateToMap.asSharedFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun initialize() {
        if (initialized) return

        // For demonstration, setting a default location if none exists.
        weatherRepository.setCurrentLocation(
            Location("Oslo Sentralstasjon", "59.9111", "10.7533")
        )
        initialized = true
        getActivitiesForToday()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHomeScreenData() {
        val location = currentLocation.value ?: return

        viewModelScope.launch {
            _homeScreenUiState.update { it.copy(isLoading = true) }

            var todaysWeather: ForecastToday? = null
            var todaysWeatherError: String? = null

            var thisWeeksWeather: List<ForecastForDay> = emptyList()
            var thisWeeksWeatherError: String? = null

            var alerts: List<AlertData> = emptyList()
            var alertsError: String? = null

            var sunRiseSet: List<String> = emptyList()
            var sunRiseSetError: String? = null

            var next24Hours: List<ForecastForHour> = emptyList()
            var next24HoursError: String? = null

            var dayIntervals: List<List<DetailedForecastForDay>> = emptyList()
            var dayIntervalsError: String? = null

            try {
                todaysWeather = weatherRepository.getForecastToday(location)
                todaysWeatherError = null
            } catch (e: Exception) {
                todaysWeatherError = e.toString()

            }

            // Fetch weekly forecast
            try {
                thisWeeksWeather = weatherRepository.getForecastByDay(location)
                thisWeeksWeatherError = null
            } catch (e: Exception) {
                thisWeeksWeatherError = e.toString()

            }

            // Fetch alerts for location
            try {
                alerts = weatherRepository.getAlertsForLocation(location)
                alertsError = null
            } catch (e: Exception) {
                alertsError = e.toString()

            }

            // Retrieve device date time and then sunrise/sunset data
            try {
                val dateTime = deviceDateTimeRepository.getDateTime()
                sunRiseSet = weatherRepository.getSunRiseData(location, dateTime)
                sunRiseSetError = null
            } catch (e: Exception) {
                sunRiseSetError = e.toString()

            }

            try {
                next24Hours = weatherRepository.getForecastForHour(location)
                next24HoursError = null
            } catch (e: Exception) {
                next24HoursError = e.toString()
            }

            try {
                dayIntervals = weatherRepository.getForecastByDayIntervals(location)
                dayIntervalsError = null
            } catch (e: Exception) {
                dayIntervalsError = e.toString()
            }


            // Update the UI state after collecting all data/errors.
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    weatherToday = todaysWeather,
                    thisWeeksWeather = thisWeeksWeather,
                    locationName = location.addressName,
                    alerts = alerts,
                    sunRiseSet = sunRiseSet,
                    next24Hours = next24Hours,
                    dayIntervals = dayIntervals,
                    todaysWeatherError = todaysWeatherError,
                    weatherTodayError = todaysWeatherError,
                    thisWeeksWeatherError = thisWeeksWeatherError,
                    alertsError = alertsError,
                    sunRiseSetError = sunRiseSetError,
                    next24HoursError = next24HoursError,
                    dayIntervalsError = dayIntervalsError
                )
            }

            // End loading state.
            _homeScreenUiState.update { it.copy(isLoading = false) }
        }
    }

    fun getActivitiesForToday() {
        viewModelScope.launch {
            _homeScreenUiState.update { it.copy(isLoadingActivitiesToday = true, isErrorActivitiesToday = false) }
            try {
                val activities = weatherRepository.getSuggestedActivitiesForOneDay(
                    currentLocation.value!!,
                    0
                )
                if (activities == null) {
                    throw Exception("Activities are null")
                }
                weatherRepository.replaceActivitiesForDay(0, activities)
                _homeScreenUiState.update {
                    it.copy(
                        isErrorActivitiesToday = false,
                        errorMessageActivitiesToday = "",
                    )
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isErrorActivitiesToday = true,
                        errorMessageActivitiesToday = e.toString() ?: "Unknown error"
                    )
                }
                Log.e("ActivityViewModel", "Error fetching today's activities: ", e)
            } finally {
                _homeScreenUiState.update { it.copy(isLoadingActivitiesToday = false) }
            }
        }
    }

    fun getActivitiesForAFutureDay(dayNr: Int) {
        viewModelScope.launch {
            _homeScreenUiState.update { it.copy(loadingFutureActivities = it.loadingFutureActivities + dayNr, isErrorFutureActivities = false) }
            try {
                weatherRepository.getSuggestedActivitiesForOneDay(
                    currentLocation.value!!,
                    dayNr
                )?.let {
                    weatherRepository.replaceActivitiesForDay(dayNr, it)
                }
                    ?: throw Exception("Activities are null")
                _homeScreenUiState.update {
                    it.copy(
                        isErrorFutureActivities = false,
                        errorMessageFutureActivities = "",
                    )
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isErrorFutureActivities = true,
                        errorMessageFutureActivities = e.toString() ?: "Unknown error"
                    )
                }
                Log.e("ActivityViewModel", "Error fetching a future days activities ", e)
            } finally {
                _homeScreenUiState.update { it.copy(loadingFutureActivities = it.loadingFutureActivities - dayNr) }
            }
        }
    }

    fun replaceActivityInDay(dayNr: Int, index: Int) {
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Replacing activity in day $dayNr at index $index")
            _homeScreenUiState.update {
                it.copy(loadingActivities = it.loadingActivities + (dayNr to index))
            }
            try {
                weatherRepository.getSuggestedActivity(currentLocation.value!!, dayNr, index)
                    ?.let {
                        weatherRepository.replaceActivityInDay(dayNr, index, it)
                    } ?: throw Exception("New activity is null")
                Log.d("HomeScreenViewModel", "Successfully replaced activity")
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error replacing activity: ", e)
            }
            finally {
                _homeScreenUiState.update { it.copy(loadingActivities = it.loadingActivities - (dayNr to index)) }
            }
        }
    }

    fun viewActivityInMap(activity: ActivitySuggestion) {
        viewModelScope.launch {
            _navigateToMap.emit(activity)
        }
    }
}
data class HomeScreenUiState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val locationName: String = "",
    val alerts: List<AlertData> = emptyList(),
    val weatherToday: ForecastToday? = null,
    val thisWeeksWeather: List<ForecastForDay> = emptyList(),
    val sunRiseSet: List<String> = emptyList(),
    val next24Hours: List<ForecastForHour> = emptyList(),
    val dayIntervals: List<List<DetailedForecastForDay>> = emptyList(),

    // errors
    val todaysWeatherError: String? = null,
    val loadingActivities: Set<Pair<Int, Int>> = emptySet(),
    // todays activities
    val isLoadingActivitiesToday: Boolean = false,
    val isErrorActivitiesToday: Boolean = false,
    val errorMessageActivitiesToday: String = "",
    // rest of the week
    val loadingFutureActivities: Set<Int> = emptySet(),
    val isErrorFutureActivities: Boolean = false,
    val errorMessageFutureActivities: String = "",
    // errors for other data
    val weatherTodayError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
    val next24HoursError: String? = null,
    val dayIntervalsError: String? = null
)
