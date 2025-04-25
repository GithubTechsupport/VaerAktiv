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
import no.uio.ifi.in2000.vaeraktiv.model.places.NearbyPlaceSuggestion
import no.uio.ifi.in2000.vaeraktiv.model.places.NearbyPlacesSuggestions
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
            listOf(
                NearbyPlaceSuggestion(
                    id = "1",
                    displayName = "Central Park",
                    formattedAddress = "Central Park, New York, NY",
                    coordinates = 40.785091 to -73.968285,
                    primaryType = "park",
                    primaryTypeDisplayName = "Park",
                    types = listOf("park", "tourist_attraction")
                ),
                NearbyPlaceSuggestion(
                    id = "2",
                    displayName = "Metropolitan Museum of Art",
                    formattedAddress = "1000 5th Ave, New York, NY",
                    coordinates = 40.779437 to -73.963244,
                    primaryType = "museum",
                    primaryTypeDisplayName = "Museum",
                    types = listOf("museum", "art_gallery")
                ),
                NearbyPlaceSuggestion(
                    id = "3",
                    displayName = "Shake Shack",
                    formattedAddress = "Madison Ave &, E 86th St, New York, NY",
                    coordinates = 40.776927 to -73.976610,
                    primaryType = "restaurant",
                    primaryTypeDisplayName = "Restaurant",
                    types = listOf("restaurant", "fast_food")
                )
            )
        )

        val dummyRoutes = RoutesSuggestions(
            listOf(
                // your original two…
                RouteSuggestion(
                    id = "route1",
                    name = "Central Park Loop",
                    distance = 10000.0,
                    elevationGain = 75.0,
                    averageGrade = 0.5,
                    polyline = "_p~iF~ps|U_ulLnnqC_mqNvxq`@",
                    startPosition = 40.785091 to -73.968285,
                    endPosition = 40.785091 to -73.968285,
                    source = RouteSource.OVERPASS
                ),
                RouteSuggestion(
                    id = "route2",
                    name = "Museum Mile Ride",
                    distance = 5000.0,
                    elevationGain = 30.0,
                    averageGrade = 0.3,
                    polyline = "a~l~Fjk~uOwHJy@P",
                    startPosition = 40.779437 to -73.963244,
                    endPosition = 40.776927 to -73.976610,
                    source = RouteSource.STRAVA
                ),

                // new longer routes…
                RouteSuggestion(
                    id = "route3",
                    name = "Reservoir Grand Circuit",
                    distance = 15000.0,              // ~15 km
                    elevationGain = 50.0,
                    averageGrade = 0.3,
                    // winds around the Jacqueline Kennedy Onassis Reservoir
                    polyline = "sz|jFjm~uOe@eCgByDoAmCgA{Ci@uBeAqA",
                    startPosition = 40.785091 to -73.968285,
                    endPosition = 40.785091 to -73.968285,
                    source = RouteSource.OVERPASS
                ),
                RouteSuggestion(
                    id = "route4",
                    name = "Full Park Perimeter Challenge",
                    distance = 21000.0,              // ~21 km
                    elevationGain = 120.0,
                    averageGrade = 0.6,
                    // an outer loop sticking to park drives
                    polyline = "}_ojFzn{uOPd@Tl@Jh@Hx@Jp@Nx@Rf@Tb@Vt@Xd@Xf@Zf@",
                    startPosition = 40.776927 to -73.976610,
                    endPosition = 40.776927 to -73.976610,
                    source = RouteSource.STRAVA
                ),
                RouteSuggestion(
                    id = "route5",
                    name = "East-West Crosscut Tour",
                    distance = 18000.0,              // ~18 km
                    elevationGain = 80.0,
                    averageGrade = 0.4,
                    // zig-zagging east-west along transverse roads and park paths
                    polyline = "klwjF`h|uO}B~@kDlAqEjBgGh@cAtA",
                    startPosition = 40.779437 to -73.963244,
                    endPosition = 40.785091 to -73.968285,
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
