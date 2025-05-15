package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable

/**
 * Represents a weather forecast block with a summary and detailed data.
 *
 * @param summary brief text summary of the forecast
 * @param details detailed parameters for the forecast period
 */
@Serializable
data class Forecast(
    val summary: Summary,
    val details: Details? = null
)
