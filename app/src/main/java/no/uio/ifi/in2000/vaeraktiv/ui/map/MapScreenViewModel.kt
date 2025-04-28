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
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
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

    init {
        val dummyPlaces = NearbyPlacesSuggestions(
            suggestions = listOf(
                NearbyPlaceSuggestion(
                    id = "start",
                    placeName = "Central Park Entrance",
                    formattedAddress = "5th Ave & E 72nd St, New York, NY",
                    coordinates = Pair(40.785091, -73.968285),
                    primaryType = "park",
                    primaryTypeDisplayName = "Park",
                    types = listOf("park", "tourist_attraction")
                ),
                NearbyPlaceSuggestion(
                    id = "end",
                    placeName = "The Lake",
                    formattedAddress = "Mid-Park at E 72nd St, New York, NY",
                    coordinates = Pair(40.789623, -73.959893),
                    primaryType = "attraction",
                    primaryTypeDisplayName = "Point of Interest",
                    types = listOf("point_of_interest")
                )
            )
        )

// Dummy route connecting the two places
        val dummyRoutes = RoutesSuggestions(
            suggestions = listOf(
                RouteSuggestion(
                    id = "route1",
                    routeName = "Central Park Walk",
                    distance = 600.0,
                    elevationGain = 5.0,
                    averageGrade = 1.0,
                    polyline = "yy|wFv|mbMi[ms@", // Encoded polyline between the two points
                    startPosition = Pair(40.785091, -73.968285),
                    endPosition = Pair(40.789623, -73.959893),
                    source = RouteSource.OVERPASS
                )
            )
        )
        fetchPlacesAndRoutes(
            places = dummyPlaces,
            routes = dummyRoutes
        )
    }

    private fun fetchPlacesAndRoutes(places: NearbyPlacesSuggestions, routes: RoutesSuggestions) {
        viewModelScope.launch {
            _mapScreenUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _mapScreenUiState.update {
                    it.copy(
                        places = places.suggestions,
                        routes = routes.suggestions,
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
    val places: List<NearbyPlaceSuggestion> = emptyList(),
    val routes: List<RouteSuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
