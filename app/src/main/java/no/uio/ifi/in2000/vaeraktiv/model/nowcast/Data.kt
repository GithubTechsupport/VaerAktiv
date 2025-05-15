package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container for instant and short-term forecasts.
 *
 * @param instant current measurement data
 * @param next1Hours forecast summary for next 1 hour
 */

@Serializable
data class Data(
    val instant: Instant,
    @SerialName("next_1_hours")
    val next1Hours: Forecast? = null,
)
