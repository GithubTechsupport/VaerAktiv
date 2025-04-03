package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
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

    fun updateCurrentLocation(location: Location) {
        weatherRepo.setCurrentLocation(location)
    }

    private fun loadLocationsAndFetchWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val locations = favoriteLocationRepo.getAllLocations()
                val favoriteData = weatherRepo.getFavoriteLocationsData(locations)
                withContext(Dispatchers.Main) {
                    _data.value = favoriteData
                }
            } catch (e: Exception) {
                // Log the exception or update UI state accordingly
                Log.e("FavoriteLocationVM", "Error fetching data", e)
            }
        }
    }
    fun addLocation(loc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteLocationRepo.addLocationByName(loc)
            loadLocationsAndFetchWeather()
        }
    }

    fun deleteLocation(loc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteLocationRepo.deleteLocationByName(loc)
            _data.value = _data.value.filterNot { it.name == loc }
            loadLocationsAndFetchWeather()
        }
    }

    fun getData() {
        loadLocationsAndFetchWeather()
    }
}