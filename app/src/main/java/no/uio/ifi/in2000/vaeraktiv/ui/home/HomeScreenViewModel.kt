package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
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

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
)