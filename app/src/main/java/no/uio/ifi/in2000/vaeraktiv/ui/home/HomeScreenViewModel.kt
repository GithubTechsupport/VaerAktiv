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
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val weatherRepository: WeatherRepository, private val deviceDateTimeRepository: DeviceDateTimeRepository) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = weatherRepository.currentLocation

    private val _homeScreenUiState = MutableStateFlow(
        HomeScreenUiState()
    )

    val homeScreenUiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()

    fun startTracking(lifecycleOwner: LifecycleOwner) {
        weatherRepository.trackDeviceLocation(lifecycleOwner)
    }

    fun initialize() {
        if (initialized) return
        weatherRepository.setCurrentLocation(Location("Oslo sentralstasjon", 59.911491, 10.757933))
        initialized = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getHomeScreenData() {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(isLoading = true)
            }
            Log.d("HomeScreenViewModel", "Getting data ${currentLocation.value}")
            try {
                Log.d("HomeScreenViewModel", "Getting data ${currentLocation.value}")
                val todaysWeather = weatherRepository.getForecastToday(currentLocation.value!!)
                val thisWeeksWeather = weatherRepository.getForecastByDay(currentLocation.value!!)
                val todaysAlerts = weatherRepository.getAlertsForLocation(currentLocation.value!!)
                val dateTime = deviceDateTimeRepository.getDateTime()
                Log.d("HomeScreenViewModel", "DateTime: $dateTime")
                val todaysSunrise = weatherRepository.getSunRiseData(currentLocation.value!!, dateTime)
                Log.d("HomeScreenViewModel", "Sunrise: $todaysSunrise")
                _homeScreenUiState.update {
                    it.copy(
                        todaysWeather = todaysWeather,
                        thisWeeksWeather = thisWeeksWeather,
                        locationName = currentLocation.value!!.addressName,
                        alerts = todaysAlerts,
                        sunRiseSet = todaysSunrise
                    )
                }
                //Log.d("HomeScreenViewModel", "Alerts: ${data}")
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.toString()
                    )
                }
                Log.d("HomeScreenViewModel", "Error: $e")
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
    val isError: Boolean = false,
    val errorMessage: String = "",
    val locationName: String = "",
    val alerts: List<AlertData> = emptyList(),
    val todaysWeather: ForecastToday? = null,
    val thisWeeksWeather: List<ForecastForDay> = emptyList(),
    val sunRiseSet : List<String> = emptyList()
)