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
import no.uio.ifi.in2000.vaeraktiv.model.ai.Interval
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val deviceDateTimeRepository: DeviceDateTimeRepository
) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = weatherRepository.currentLocation

    private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    fun startTracking(lifecycleOwner: LifecycleOwner) {
        weatherRepository.trackDeviceLocation(lifecycleOwner)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initialize() {
        if (initialized) return

        // For demonstration, setting a default location if none exists.
        weatherRepository.setCurrentLocation(
            Location("Oslo Sentralstasjon", "59.9111", "10.7533")
        )
        initialized = true
        getActivities() // fra ActivityScreenViewModel
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


            // Update the UI state after collecting all data/errors.
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    weatherToday = todaysWeather,
                    thisWeeksWeather = thisWeeksWeather,
                    locationName = location.addressName,
                    alerts = alerts,
                    sunRiseSet = sunRiseSet,
                    next24Hours = next24Hours,
                    todaysWeatherError = todaysWeatherError,
                    weatherTodayError = todaysWeatherError,
                    thisWeeksWeatherError = thisWeeksWeatherError,
                    alertsError = alertsError,
                    sunRiseSetError = sunRiseSetError,
                    next24HoursError = next24HoursError
                )
            }

            // End loading state.
            _homeScreenUiState.update { it.copy(isLoading = false) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getActivities() { // fra ActivityScreenViewModel
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(isLoadingActivitiesToday = true)
            }
            try {
                val today = LocalDate.now()
                val activities = weatherRepository.getActivitiesForDate(
                    currentLocation.value ?: throw Exception("No location"),
                    today
                )
                _homeScreenUiState.update {
                    it.copy(
                        activitiesToday = activities,
                        isErrorActivitiesToday = false,
                        errorMessageActivitiesToday = "",
                    )
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isErrorFutureActivities = true,
                        errorMessageFutureActivities = e.toString() ?: "Unknown error"
                    )

                }
                Log.e("ActivityViewModel", "Error fetching todays activites: ", e)
            } finally {
                _homeScreenUiState.update {
                    it.copy()
                }
            }
        }
    }

    fun getActivitiesForDate(date: LocalDate) {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(loadingFutureActivities = it.loadingFutureActivities + date)
            }
            try {
                val activities = weatherRepository.getActivitiesForDate(
                    currentLocation.value!!,
                    date
                )
                _homeScreenUiState.update {
                    it.copy(
                        futureActivities = it.futureActivities + (date to activities),
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
                Log.e("ActivityViewModel", "Error fetching activites for $date: ", e)
            } finally {
                _homeScreenUiState.update {
                    it.copy(loadingFutureActivities = it.loadingFutureActivities - date)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshSingleActivity(index: Int) {
        viewModelScope.launch {
            Log.d("HomeScreenViewModel", "Starting refresh for index $index")
            val startTime = System.currentTimeMillis()
            _homeScreenUiState.update {
                it.copy(isRefreshingActivity = it.isRefreshingActivity + index)
            }
            Log.d(
                "HomeScreenViewModel",
                "isRefreshingActivity after adding index: ${_homeScreenUiState.value.isRefreshingActivity}"
            )

            var newInterval: Interval? = null
            var errorString: String = ""
            try {
                val today = LocalDate.now()
                val location = currentLocation.value ?: throw Exception("No location")
                Log.d("HomeScreenViewModel", "Location: $location")
                Log.d(
                    "HomeScreenViewModel",
                    "todaysActivities before refresh: ${_homeScreenUiState.value.activitiesToday}"
                )
                newInterval = weatherRepository.getNewActivityForDate(location, today)
            } catch (e: Exception) {
                Log.d("HomeScreenViewModel", "Error refreshing activity at index: $index")
                _homeScreenUiState.update {
                    it.copy(
                        isErrorActivitiesToday = true,
                        errorMessageActivitiesToday = e.toString(),
                        isRefreshingActivity = it.isRefreshingActivity - index
                    )
                }
                Log.e("Melding fra HomeScreenViewModel", "Error fetching todays activites: ", e)
            }

            val elapsedTime = System.currentTimeMillis() - startTime
            val minDuration = 1000L
            if (elapsedTime < minDuration) {
                delay(minDuration - elapsedTime)
                Log.d("HomeScreenViewModel", "Delaying for $minDuration ms")
            } else {
                Log.d("HomeScreenViewModel", "No delay needed")
            }

            _homeScreenUiState.update { currentState ->
                if (currentState.errorMessage != null) {
                    currentState.copy(
                        isErrorActivitiesToday = true,
                        errorMessageActivitiesToday = errorString,
                        isRefreshingActivity = currentState.isRefreshingActivity - index
                    )
                } else if (newInterval != null) {
                    val currentActivities =
                        currentState.activitiesToday?.activities?.toMutableList() ?: mutableListOf()
                    if (index in currentActivities.indices) {
                        currentActivities[index] = newInterval

                        Log.d(
                            "HomeScreenViewModel",
                            "Successfully updated activity at index $index"
                        )
                        currentState.copy(
                            activitiesToday = JsonResponse(activities = currentActivities),
                            isErrorActivitiesToday = false,
                            errorMessageActivitiesToday = "",
                            isRefreshingActivity = currentState.isRefreshingActivity - index
                        )
                    } else {
                        Log.w(
                            "HomeScreenViewModel",
                            "Index $index out of bounds for activitiesToday: ${currentActivities.size}"
                        )
                        currentState.copy(
                            isErrorActivitiesToday = true,
                            errorMessageActivitiesToday = "Index $index out of bounds",
                            isRefreshingActivity = currentState.isRefreshingActivity - index
                        )
                    }
                } else {
                    Log.w("HomeScreenViewModel", "Index $index out of bounds for activitiesToday:")
                    currentState.copy(
                        isRefreshingActivity = currentState.isRefreshingActivity - index
                    )
                }
            }
            Log.d(
                "HomeScreenViewModel",
                "After refresh, isRefreshingActivity: ${_homeScreenUiState.value.isRefreshingActivity}"
            )
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

    // errors
    val todaysWeatherError: String? = null,
    val isRefreshingActivity: Set<Int> = emptySet(),
    // todays activities
    val activitiesToday: JsonResponse? = null, // fra ActivityScreenViewModel
    val isLoadingActivitiesToday: Boolean = false,
    val isErrorActivitiesToday: Boolean = false,
    val errorMessageActivitiesToday: String = "",
    // rest of the week
    val futureActivities: Map<LocalDate, JsonResponse> = emptyMap(), // fra ActivityScreenViewModel
    val loadingFutureActivities: Set<LocalDate> = emptySet(),
    val isErrorFutureActivities: Boolean = false,
    val errorMessageFutureActivities: String = "",
    // errors for other data
    val weatherTodayError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
    val next24HoursError: String? = null
    val sunRiseSetError: String? = null,
)
