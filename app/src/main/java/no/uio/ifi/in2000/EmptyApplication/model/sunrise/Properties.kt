package no.uio.ifi.in2000.EmptyApplication.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val body: String,
    val sunrise: SunEvent,
    val sunset: SunEvent,
    val solarnoon: SolarEvent,
    val solarmidnight: SolarEvent
)
