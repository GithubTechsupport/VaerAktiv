package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.home.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastToday
import javax.inject.Inject

/**
 * ViewModel for the home screen, managing weather data, activity suggestions,
 * UI state, and navigation events.
 */
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val aggregateRepository: IAggregateRepository,
    private val deviceDateTimeRepository: DeviceDateTimeRepository
) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = aggregateRepository.currentLocation
    val deviceLocation: LiveData<Location?> = aggregateRepository.deviceLocation
    val activities: LiveData<List<SuggestedActivities?>> = aggregateRepository.activities

    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    private val _navigateToMap = MutableSharedFlow<ActivitySuggestion>()
    val navigateToMap = _navigateToMap.asSharedFlow()

    private val _navigateToPreferences = MutableLiveData(false)
    val navigateToPreferences: LiveData<Boolean> get() = _navigateToPreferences

    /**
     * Initializes default location and UI state once.
     */
    fun initialize() {
        if (!initialized) {
            Log.d("HomeScreen", "Initializing")
            _homeScreenUiState.update { it.copy(isLoading = true) }
            aggregateRepository.setCurrentLocation(Location("Oslo", "59.914", "10.752"))
            _homeScreenUiState.update { it.copy(isLoading = false) }
            initialized = true
        }
    }

    /** Updates current location in repository. */
    fun setCurrentLocation(location: Location) = aggregateRepository.setCurrentLocation(location)

    /** Resets navigation flag after preferences screen handled. */
    fun onNavigationHandled() {
        _navigateToPreferences.value = false
    }


    /** Refreshes all home screen data and activities. */
    fun resetScreenState() {
        getHomeScreenData()
        resetActivities()
        getActivitiesForToday()
    }

    private fun resetActivities() {
        viewModelScope.launch {
            aggregateRepository.resetActivities()
        }
    }

    /**
     * Fetches weather data, alerts, sunrise/sunset, and updates UI state.
     */
    fun getHomeScreenData() {
        val location = currentLocation.value ?: return

        viewModelScope.launch {
            _homeScreenUiState.update { it.copy(isLoading = true) }

            var todaysWeather: ForecastToday? = null
            var todaysWeatherError: String?

            var thisWeeksWeather: List<ForecastForDay> = emptyList()
            var thisWeeksWeatherError: String?

            var alerts: List<AlertData> = emptyList()
            var alertsError: String?

            var sunRiseSet: List<String> = emptyList()
            var sunRiseSetError: String?

            var next24Hours: List<ForecastForHour> = emptyList()
            var next24HoursError: String?

            var dayIntervals: List<List<DetailedForecastForDay>> = emptyList()
            var dayIntervalsError: String?

            try {
                todaysWeather = aggregateRepository.getForecastToday(location)
                todaysWeatherError = null
            } catch (e: Exception) {
                todaysWeatherError = e.toString()
            }

            try {
                thisWeeksWeather = aggregateRepository.getForecastByDay(location)
                thisWeeksWeatherError = null
            } catch (e: Exception) {
                thisWeeksWeatherError = e.toString()
            }

            try {
                alerts = aggregateRepository.getAlertsForLocation(location)
                alertsError = null
            } catch (e: Exception) {
                alertsError = e.toString()
            }

            try {
                val dateTime = deviceDateTimeRepository.getDateTime()
                sunRiseSet = aggregateRepository.getSunRiseData(location, dateTime)
                sunRiseSetError = null
            } catch (e: Exception) {
                sunRiseSetError = e.toString()
            }

            try {
                next24Hours = aggregateRepository.getForecastForHour(location)
                next24HoursError = null
            } catch (e: Exception) {
                next24HoursError = e.toString()
            }

            try {
                dayIntervals = aggregateRepository.getForecastByDayIntervals(location)
                dayIntervalsError = null
            } catch (e: Exception) {
                dayIntervalsError = e.toString()
            }

            // Update UI state with fetched data and errors
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

    /** Fetches today's activity suggestions and updates repository. */
    private fun getActivitiesForToday() {
        viewModelScope.launch {
            _homeScreenUiState.update { it.copy(isLoadingActivitiesToday = true, isErrorActivitiesToday = false) }
            try {
                val activities = aggregateRepository.getSuggestedActivitiesForOneDay(
                    currentLocation.value!!, 0
                ) ?: throw Exception("Activities are null")

                aggregateRepository.replaceActivitiesForDay(0, activities)
                _homeScreenUiState.update {
                    it.copy(isErrorActivitiesToday = false, errorMessageActivitiesToday = "")
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(isErrorActivitiesToday = true, errorMessageActivitiesToday = e.toString())
                }
                Log.e("ActivityViewModel", "Error fetching today's activities: ", e)
            } finally {
                _homeScreenUiState.update { it.copy(isLoadingActivitiesToday = false) }
            }
        }
    }

    /** Fetches activity suggestions for a future day and updates repository. */
    fun getActivitiesForAFutureDay(dayNr: Int) {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(
                    loadingFutureActivities = it.loadingFutureActivities + dayNr,
                    isErrorFutureActivities = false
                )
            }
            try {
                aggregateRepository.getSuggestedActivitiesForOneDay(
                    currentLocation.value!!, dayNr
                )?.let {
                    aggregateRepository.replaceActivitiesForDay(dayNr, it)
                } ?: throw Exception("Activities are null")

                _homeScreenUiState.update {
                    it.copy(isErrorFutureActivities = false, errorMessageFutureActivities = "")
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(isErrorFutureActivities = true, errorMessageFutureActivities = e.toString())
                }
                Log.e("ActivityViewModel", "Error fetching a future day's activities", e)
            } finally {
                _homeScreenUiState.update {
                    it.copy(loadingFutureActivities = it.loadingFutureActivities - dayNr)
                }
            }
        }
    }

    /**
     * Replaces a specific activity suggestion for a given day and index.
     */
    fun replaceActivityInDay(dayNr: Int, index: Int) {
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Replacing activity in day $dayNr at index $index")
            _homeScreenUiState.update {
                it.copy(loadingActivities = it.loadingActivities + (dayNr to index))
            }
            try {
                aggregateRepository.getSuggestedActivity(currentLocation.value!!, dayNr, index)
                    ?.let {
                        aggregateRepository.replaceActivityInDay(dayNr, index, it)
                    } ?: throw Exception("New activity is null")

                Log.d("HomeScreenViewModel", "Successfully replaced activity")
            } catch (e: Exception) {
                Log.e("HomeScreenViewModel", "Error replacing activity: ", e)
            } finally {
                _homeScreenUiState.update {
                    it.copy(loadingActivities = it.loadingActivities - (dayNr to index))
                }
            }
        }
    }

    /** Emits navigation event to show the activity on the map. */
    fun viewActivityInMap(activity: ActivitySuggestion) {
        viewModelScope.launch {
            _navigateToMap.emit(activity)
        }
    }

    /** Triggers navigation to the preferences screen. */
    fun navigateToPreferences() {
        viewModelScope.launch {
            _navigateToPreferences.value = true
        }
    }
}

/**
 * UI state for the home screen, including weather data, activity states,
 * loading indicators, and error messages.
 */
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

    // Errors and loading state for activities
    val todaysWeatherError: String? = null,
    val loadingActivities: Set<Pair<Int, Int>> = emptySet(),

    val isLoadingActivitiesToday: Boolean = false,
    val isErrorActivitiesToday: Boolean = false,
    val errorMessageActivitiesToday: String = "",

    val loadingFutureActivities: Set<Int> = emptySet(),
    val isErrorFutureActivities: Boolean = false,
    val errorMessageFutureActivities: String = "",

    // Errors for weather and alerts
    val weatherTodayError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
    val next24HoursError: String? = null,
    val dayIntervalsError: String? = null
)
