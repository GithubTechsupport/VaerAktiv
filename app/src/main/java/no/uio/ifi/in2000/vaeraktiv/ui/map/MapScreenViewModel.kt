package no.uio.ifi.in2000.vaeraktiv.ui.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlaceActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

/**
 * ViewModel managing map UI state, including places, routes, and activity selection.
 */
@HiltViewModel
class MapScreenViewModel @Inject constructor(
    aggregateRepository: IAggregateRepository
) : ViewModel() {

    private val _mapScreenUiState = MutableStateFlow(MapScreenUiState())
    val mapScreenUiState: StateFlow<MapScreenUiState> = _mapScreenUiState.asStateFlow()

    val activities: LiveData<List<SuggestedActivities?>> = aggregateRepository.activities

    /**
     * Decodes an encoded polyline into GeoPoints.
     *
     * @throws Exception if decoding fails.
     */
    fun decodePolyline(encoded: String): List<GeoPoint> {
        try {
            val points = mutableListOf<GeoPoint>()
            var index = 0
            var lat = 0
            var lng = 0

            while (index < encoded.length) {
                var result = 0
                var shift = 0
                var b: Int

                do {
                    b = encoded[index++].code - 63
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlat = if (result and 1 != 0) (result.inv() shr 1) else (result shr 1)
                lat += dlat

                result = 0
                shift = 0

                do {
                    b = encoded[index++].code - 63
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlng = if (result and 1 != 0) (result.inv() shr 1) else (result shr 1)
                lng += dlng

                points.add(GeoPoint(lat / 1e5, lng / 1e5))
            }

            return points
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Updates UI state with place and route suggestions from the provided list.
     */
    fun updatePlacesAndRoutes(suggestedActivitiesList: List<SuggestedActivities?>) {
        viewModelScope.launch {
            _mapScreenUiState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val allActivities = suggestedActivitiesList
                    .filterNotNull()
                    .flatMap { it.activities }

                val places = allActivities.filterIsInstance<PlaceActivitySuggestion>()
                val routes = allActivities.filterIsInstance<StravaActivitySuggestion>()

                _mapScreenUiState.update {
                    it.copy(
                        places = places,
                        routes = routes,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _mapScreenUiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    /**
     * Sets selectedActivityPoints to zoom in on the given activity.
     */
    fun zoomInOnActivity(activity: ActivitySuggestion) {
        try {
            val points = when(activity) {
                is PlaceActivitySuggestion ->
                    listOf(GeoPoint(activity.coordinates.first, activity.coordinates.second))
                is StravaActivitySuggestion ->
                    decodePolyline(activity.polyline)
                else -> emptyList()
            }
            _mapScreenUiState.update { it.copy(selectedActivityPoints = points) }
        } catch (e: Exception) {
            Log.e("MapScreenViewModel", "Error zooming in on activity", e)
            return
        }
    }

    /** Clears any selected activity points on the map. */
    fun clearSelectedActivityPoints() {
        _mapScreenUiState.update { it.copy(selectedActivityPoints = null) }
    }
}

/**
 * UI state for the map screen, holding loading flags, data and optional errors.
 */
data class MapScreenUiState(
    val places: List<PlaceActivitySuggestion> = emptyList(),
    val routes: List<StravaActivitySuggestion> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    val selectedActivityPoints: List<GeoPoint>? = null
)