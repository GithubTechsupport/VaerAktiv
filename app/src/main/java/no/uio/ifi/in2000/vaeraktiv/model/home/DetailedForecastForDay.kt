package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Represents a detailed weather forecast for a given interval.
 *
 * @param date the date of the forecast (ISO format)
 * @param interval time range within the day (e.g., "06:00–12:00")
 * @param icon weather icon identifier
 */
data class DetailedForecastForDay(
    val date: String,
    val interval: String,
    val icon: String
)
