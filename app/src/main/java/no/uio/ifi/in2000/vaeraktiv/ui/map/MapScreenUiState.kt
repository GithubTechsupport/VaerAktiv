package no.uio.ifi.in2000.vaeraktiv.ui.map

import no.uio.ifi.in2000.vaeraktiv.model.ai.PlaceActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import org.osmdroid.util.GeoPoint

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