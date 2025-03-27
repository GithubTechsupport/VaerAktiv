package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository

class HomeViewModel (weatherRepository: WeatherRepository) : ViewModel() {
//    private val weatherData = weatherRepository.getWeatherData()
//
//    private val _location = MutableLiveData<String>()
//    val location : LiveData<String> = _location
//
//    init {
//        getWeatherForLocation(Location.String)
//    }
//
//    private fun getWeatherForLocation(location : String) {
//        viewModelScope.launch {
//            val data = weatherData.getWeatherForLocation(location)
//        }
//    }
}
