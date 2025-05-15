package no.uio.ifi.in2000.vaeraktiv.model.ai.places

/**
 * Data class representing a list of suggestions for nearby places.
 * Along with a formatted string representation of the suggestions for the prompt.
 *
 * @property suggestions A list of nearby place suggestions.
 */
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

