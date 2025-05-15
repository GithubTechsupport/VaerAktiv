package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Represents a sunlight event with precise azimuth.
 *
 * @param time ISO timestamp of the event
 * @param azimuth angle of the sun above horizon in degrees
 */
@Serializable
data class SunEvent(
    val time: String,
    val azimuth: Double
)
