package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Contains detailed solar event timestamps.
 *
 * @param body descriptive text or metadata
 * @param sunrise sunrise event
 * @param sunset sunset event
 * @param solarnoon solar noon event
 * @param solarmidnight solar midnight event
 */
@Serializable
data class SunProperties(
    val body: String,
    val sunrise: SunEvent,
    val sunset: SunEvent,
    val solarnoon: SolarEvent,
    val solarmidnight: SolarEvent
)
