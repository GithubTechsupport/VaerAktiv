package no.uio.ifi.in2000.vaeraktiv.model.ai.places

import kotlinx.serialization.Serializable

data class NearbyPlacesSuggestions(
    val suggestions: List<NearbyPlaceSuggestion>
) {
    override fun toString(): String {
        return suggestions.joinToString(separator = "\n\n") {
            """
Place name: ${it.placeName}
Place address: ${it.formattedAddress} 
Place primary type: ${it.primaryType}
Place types: ${it.types?.joinToString(", ")}"                
            """.trimIndent()
        }
    }
}

data class NearbyPlaceSuggestion(
    val id: String? = null,
    val placeName: String? = null,
    val formattedAddress: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val primaryType: String? = null,
    val primaryTypeDisplayName: String? = null,
    val types: List<String>? = emptyList()
)
