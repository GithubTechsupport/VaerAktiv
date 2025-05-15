package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Represents a solar event (sunrise or sunset) occurrence.
 *
 * @param time ISO timestamp of the event
 * @param visible true if the event is visible at the given time
 */
@Serializable
data class SolarEvent(
    val time: String,
    val visible: Boolean
)
