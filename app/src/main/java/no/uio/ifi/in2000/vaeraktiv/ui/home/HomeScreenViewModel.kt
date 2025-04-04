package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
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
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityScreenUiState
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

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
        weatherRepository.setCurrentLocation(Location("Oslo Sentralstasjon","59.9111", "10.7533"))
        initialized = true
    }

    fun getHomeScreenData() {
        viewModelScope.launch {
            _homeScreenUiState.update {
                it.copy(isLoading = true)
            }
            try {
                //val alerts = weatherRepository.getAlertsForLocation(currentLocation.value!!)
                val todaysWeather = weatherRepository.getForecastToday(currentLocation.value!!)
                val thisWeeksWeather = weatherRepository.getForecastByDay(currentLocation.value!!)
                _homeScreenUiState.update {
                    it.copy(
                        //alerts = alerts,
                        todaysWeather = todaysWeather,
                        thisWeeksWeather = thisWeeksWeather,
                        locationName = currentLocation.value!!.addressName
                    )
                }
            } catch (e: Exception) {
                _homeScreenUiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.toString() ?: "Unknown error"
                    )
                }
                Log.d("HomeScreenViewModel", "Error: ${e}")
            } finally {
                _homeScreenUiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }

        /*
    private val weatherData = weatherRepository.getWeatherData()

    private val _weatherToday = MutableLiveData<Details>(mutableStateListOf())
    val data : LiveData<List<Details>> = _weatherToday

    private val _weatherThisWeek = MutableLiveData<String>()
    val data : LiveData<String> = _weatherThisWeek

    init {
        getDataToday(Location.String)
    }

    private fun getDataToday(location : String) {
        _weatherToday.value = weatherData.getWeatherForLocation(location)
        loadWeatherWeek(location)
    }

    private fun loadWeatherWeek(location : String) {
        viewModelScope.launch {
            val data = weatherData.getWeatherForLocation(location)
            _weatherThisWeek.value = data
        }
    }
    */
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
)