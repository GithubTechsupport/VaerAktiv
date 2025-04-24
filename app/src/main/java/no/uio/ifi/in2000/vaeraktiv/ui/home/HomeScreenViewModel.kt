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

    fun getActivities() { // fra ActivityScreenViewModel
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(isLoading = true)
            }
            try {
                val activities = weatherRepository.getActivities(currentLocation.value!!)
                _homeScreenUiState.update {
                    it.copy(
                        activities = activities
                    )
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.toString() ?: "Unknown error"
                    )

                }
                Log.e("ActivityViewModel", "Error: ", e)
            } finally {
                _homeScreenUiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val locationName: String = "",
    val alerts: List<AlertData> = emptyList(),
    val todaysWeather: ForecastToday? = null,
    val thisWeeksWeather: List<ForecastForDay> = emptyList(),
    val sunRiseSet: List<String> = emptyList(),

    // errors
    val todaysWeatherError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
    val isError: Boolean = false, // fra Activity
    val errorMessage: String = "", // fra Actitivty
    val activities: JsonResponse? = null // fra ActivityScreenViewModel
)
