package no.uio.ifi.in2000.vaeraktiv.model.ai.places

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

