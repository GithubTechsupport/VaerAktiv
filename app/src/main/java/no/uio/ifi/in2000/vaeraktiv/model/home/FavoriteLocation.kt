package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Represents a bookmarked location with its current weather summary.
 *
 * @param name display name of the location
 * @param iconDesc description for the weather icon
 * @param shortDesc brief weather description
 * @param highestTemp daily high temperature
 * @param lowestTemp daily low temperature
 * @param downPour precipitation description
 * @param uv UV index value
 * @param wind wind speed description
 * @param lat latitude coordinate as string
 * @param lon longitude coordinate as string
 */
data class FavoriteLocation(
    val name: String,
    val iconDesc: String?,
    val shortDesc: String?,
    val highestTemp: String?,
    val lowestTemp: String?,
    val downPour: String?,
    val uv: String?,
    val wind: String?,
    val lat: String,
    val lon: String
)
