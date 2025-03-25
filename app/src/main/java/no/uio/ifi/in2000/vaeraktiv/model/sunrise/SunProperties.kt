package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SunProperties(
    val body: String,
    val sunrise: SunEvent,
    val sunset: SunEvent,
    val solarnoon: SolarEvent,
    val solarmidnight: SolarEvent
)
