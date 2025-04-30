package no.uio.ifi.in2000.vaeraktiv.ui.map

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
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlacesActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlaceSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _mapScreenUiState = MutableStateFlow(MapScreenUiState())
    val mapScreenUiState: StateFlow<MapScreenUiState> = _mapScreenUiState.asStateFlow()

    val deviceLocation: LiveData<Location?> = weatherRepository.deviceLocation

    val activities: LiveData<List<SuggestedActivities?>?> = weatherRepository.activities

    init {
    }

    fun updatePlacesAndRoutes(suggestedActivities: SuggestedActivities) {
        viewModelScope.launch {
            _mapScreenUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val places = suggestedActivities.activities.filterIsInstance<PlacesActivitySuggestion>()
                val routes = suggestedActivities.activities.filterIsInstance<StravaActivitySuggestion>()
                _mapScreenUiState.update {
                    it.copy(
                        places = places,
                        routes = routes,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _mapScreenUiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }

    }
}

data class MapScreenUiState(
    val places: List<PlacesActivitySuggestion> = emptyList(),
    val routes: List<StravaActivitySuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
