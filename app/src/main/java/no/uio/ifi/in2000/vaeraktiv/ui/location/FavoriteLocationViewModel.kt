package no.uio.ifi.in2000.vaeraktiv.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation
import javax.inject.Inject


@HiltViewModel
class FavoriteLocationViewModel @Inject constructor(
    private val weatherRepo: WeatherRepository,
    private val favoriteLocationRepo: FavoriteLocationRepository
) : ViewModel() {

    private val _data = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    val data: StateFlow<List<FavoriteLocation>> = _data.asStateFlow()

    init {
        loadLocationsAndFetchWeather()
    }

    private fun loadLocationsAndFetchWeather() {
        viewModelScope.launch {
            val locations = favoriteLocationRepo.getAllLocations()
            _data.value = weatherRepo.getFavoriteLocationsData(locations)
        }
    }

    fun addLocation(loc: String) {
        viewModelScope.launch {
            favoriteLocationRepo.addLocationByName(loc)
            loadLocationsAndFetchWeather()
        }
    }

    fun deleteLocation(loc: String) {
        viewModelScope.launch {
            favoriteLocationRepo.deleteLocationByName(loc)
            _data.value = _data.value.filterNot { it.name == loc }
        }
    }

    fun getData() {
        loadLocationsAndFetchWeather()
    }
}