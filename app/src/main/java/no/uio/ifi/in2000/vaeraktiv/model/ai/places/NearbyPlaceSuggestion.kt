package no.uio.ifi.in2000.vaeraktiv.model.ai.places

/**
 * Data class representing a suggestion for a nearby place which is included in the prompt.
 *
 * @property id The unique identifier for the place.
 * @property placeName The name of the place.
 * @property formattedAddress The formatted address of the place.
 * @property coordinates The geographical coordinates (latitude, longitude) of the place.
 * @property primaryType The primary type of the place (e.g., restaurant, park).
 * @property primaryTypeDisplayName The display name of the primary type.
 * @property types A list of additional types associated with the place.
 */
data class NearbyPlaceSuggestion(
    val id: String? = null,
    val placeName: String? = null,
    val formattedAddress: String? = null,
    val coordinates: Pair<Double, Double>? = null,
    val primaryType: String? = null,
    val primaryTypeDisplayName: String? = null,
    val types: List<String>? = emptyList()
)
