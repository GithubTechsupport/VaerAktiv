package no.uio.ifi.in2000.EmptyApplication.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SunEvent(
    val time: String,
    val azimuth: Double
)
