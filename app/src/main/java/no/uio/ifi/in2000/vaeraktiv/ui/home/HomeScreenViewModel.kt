package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
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


            // Update the UI state after collecting all data/errors.
            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    todaysWeather = todaysWeather,
                    thisWeeksWeather = thisWeeksWeather,
                    locationName = location.addressName,
                    alerts = alerts,
                    sunRiseSet = sunRiseSet,
                    todaysWeatherError = todaysWeatherError,
                    thisWeeksWeatherError = thisWeeksWeatherError,
                    alertsError = alertsError,
                    sunRiseSetError = sunRiseSetError
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
                it.copy(isLoadingTodaysActivites = true)
            }
            try {
                val today = LocalDate.now()
                val activities = weatherRepository.getActivitiesForDate(
                    currentLocation.value ?: throw Exception("No location"),
                    today
                )
                _homeScreenUiState.update {
                    it.copy(
                        todaysActivites = activities,
                        isErrorTodaysActivites = false,
                        errorMessageTodaysActivites = "",
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

    fun getActivitesForDate(date: LocalDate) {
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
}

data class HomeScreenUiState(
    val todayActivities: JsonResponse? = null,
    val isLoadingTodayActivities: Boolean = false,
    val isErrorTodayActivities: Boolean = false,
    val errorMessageTodayActivities: String = "",
    val isLoading: Boolean = false,
    val locationName: String = "",
    val alerts: List<AlertData> = emptyList(),
    val todaysWeather: ForecastToday? = null,
    val thisWeeksWeather: List<ForecastForDay> = emptyList(),
    val sunRiseSet: List<String> = emptyList(),
    // todays activities
    val todaysActivites: JsonResponse? = null, // fra ActivityScreenViewModel
    val isLoadingTodaysActivites: Boolean = false,
    val isErrorTodaysActivites: Boolean = false,
    val errorMessageTodaysActivites: String = "",
    // rest of the week
    val futureActivities: Map<LocalDate, JsonResponse> = emptyMap(), // fra ActivityScreenViewModel
    val loadingFutureActivities: Set<LocalDate> = emptySet(),
    val isErrorFutureActivities: Boolean = false,
    val errorMessageFutureActivities: String = "",
    // errors for other data
    val todaysWeatherError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
)
