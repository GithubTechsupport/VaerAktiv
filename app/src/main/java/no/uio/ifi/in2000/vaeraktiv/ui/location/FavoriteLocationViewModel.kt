package no.uio.ifi.in2000.vaeraktiv.ui.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation



class FavoriteLocationViewModel(): ViewModel() {

    val weatherRepo = WeatherRepository(null, LocationForecastRepository(LocationForecastDataSource()), null)

    private val _data = MutableStateFlow<MutableList<FavoriteLocation>>(mutableListOf())
    val data: StateFlow<List<FavoriteLocation>> = _data

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            var favoriteLocations: MutableList<FavoriteLocation> = weatherRepo.getFavoriteLocationsData()
            _data.value = favoriteLocations
        }
    }
}