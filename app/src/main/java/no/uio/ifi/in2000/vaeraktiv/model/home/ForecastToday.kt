package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Current and daily summary weather for today.
 *
 * @param tempNow current temperature
 * @param tempMax today's maximum temperature
 * @param tempMin today's minimum temperature
 * @param uv current UV index
 * @param windSpeed current wind speed
 * @param precipitationAmount expected precipitation today
 * @param icon today's overall weather icon
 * @param iconNow current weather icon
 */
data class ForecastToday(
    val tempNow: String? = null,
    val tempMax: String? = null,
    val tempMin: String? = null,
    val uv: String? = null,
    val windSpeed: String? = null,
    val precipitationAmount: String? = null,
    val icon: String? = null,
    val iconNow: String? = null
)
