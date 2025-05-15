package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Hourly weather forecast entry.
 *
 * @param time local time string (e.g., "14:00")
 * @param temp temperature at the hour
 * @param windSpeed wind speed at the hour
 * @param precipitationAmount precipitation amount
 * @param icon weather icon identifier
 * @param uv UV index value
 */
data class ForecastForHour(
    val time: String? = null,
    val temp: String? = null,
    val windSpeed: String? = null,
    val precipitationAmount: String? = null,
    val icon: String? = null,
    val uv: String? = null
)
