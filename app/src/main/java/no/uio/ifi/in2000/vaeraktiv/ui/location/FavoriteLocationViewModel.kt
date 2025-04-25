package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
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
    private val favoriteLocationRepo: FavoriteLocationRepository,
) : ViewModel() {

    // Existing state for favorite locations
    private val _data = MutableStateFlow<List<FavoriteLocation>>(emptyList())
    val data: StateFlow<List<FavoriteLocation>> = _data.asStateFlow()

    // Navigation flag
    private val _navigateToHome = MutableLiveData(false)
    val navigateToHome: LiveData<Boolean> get() = _navigateToHome

    // New state for autocomplete predictions
    private val _predictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = _predictions.asStateFlow()

    private var sessionToken : AutocompleteSessionToken? = null

    init {
        loadLocationsAndFetchWeather()
    }

    fun updateCurrentLocation(location: Location) {
        weatherRepo.setCurrentLocation(location)
        _navigateToHome.value = true
    }

    fun onNavigationHandled() {
        _navigateToHome.value = false
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
                e.printStackTrace()
            }
        }
    }

    fun addLocation(loc: String) {
        sessionToken = null
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

    fun fetchPredictions(query: String) {
        Log.d("FavoriteLocationViewModel", "fetchPredictions called with query: $query")
        if (query.isBlank()) {
            _predictions.value = emptyList()
            sessionToken = null
            Log.d("FavoriteLocationViewModel", "fetchPredictions returned with predictions: ${_predictions.value}")
            return
        }
        if (sessionToken == null) {
            sessionToken = AutocompleteSessionToken.newInstance()
        }
        val token = sessionToken!!
        viewModelScope.launch(Dispatchers.IO) {
            val response = weatherRepo.getAutocompletePredictions(query, token)
            withContext(Dispatchers.Main) {
                _predictions.value = response
            }
        }
    }
}
