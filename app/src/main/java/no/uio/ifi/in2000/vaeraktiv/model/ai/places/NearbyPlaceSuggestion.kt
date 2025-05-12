package no.uio.ifi.in2000.vaeraktiv.model.ai.places

data class NearbyPlaceSuggestion(
    val id: String? = null,
    val placeName: String? = null,
    val formattedAddress: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val primaryType: String? = null,
    val primaryTypeDisplayName: String? = null,
    val types: List<String>? = emptyList()
)
