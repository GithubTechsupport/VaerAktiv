package no.uio.ifi.in2000.vaeraktiv.model.places

import kotlinx.serialization.Serializable

data class NearbyPlacesSuggestions(
    val suggestions: List<NearbyPlaceSuggestion>
) {
    override fun toString(): String {
        return suggestions.joinToString(separator = "\n") {
            "${it.displayName} - ${it.formattedAddress} - ${it.primaryTypeDisplayName} - ${it.primaryType} - ${it.types?.joinToString(", ")}"
        }
    }
}

data class NearbyPlaceSuggestion(
    val id: String? = null,
    val displayName: String? = null,
    val formattedAddress: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val primaryType: String? = null,
    val primaryTypeDisplayName: String? = null,
    val types: List<String>? = emptyList()
)
