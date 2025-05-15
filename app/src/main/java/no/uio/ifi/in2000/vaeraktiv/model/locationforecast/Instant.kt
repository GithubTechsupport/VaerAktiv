package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.Serializable

/**
 * Encapsulates instantaneous meteorological details.
 *
 * @param details detailed parameters (temperature, wind, etc.) at this instant
 */
@Serializable
data class Instant(
    val details: Details
)
