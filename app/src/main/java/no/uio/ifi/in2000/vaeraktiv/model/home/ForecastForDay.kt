package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Summary weather forecast for a whole day.
 *
 * @param date the date of forecast (ISO format)
 * @param maxTemp maximum temperature string (with unit)
 * @param icon weather icon identifier for the day
 */
data class ForecastForDay(
    val date: String,
    val maxTemp: String,
    val icon: String
)
