package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container for instant and short-term forecasts.
 *
 * @param instant current measurement data
 * @param next12Hours forecast summary for next 12 hours
 * @param next1Hours forecast summary for next 1 hour
 * @param next6Hours forecast summary for next 6 hours
 */

@Serializable
data class Data(
    val instant: Instant,
    @SerialName("next_12_hours")
    val next12Hours: Forecast? = null,
    @SerialName("next_1_hours")
    val next1Hours: Forecast? = null,
    @SerialName("next_6_hours")
    val next6Hours: Forecast? = null
)
