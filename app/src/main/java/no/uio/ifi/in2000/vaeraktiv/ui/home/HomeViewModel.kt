package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository

class HomeViewModel (weatherRepository: WeatherRepository) : ViewModel() {
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
