package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SunGeometry(
    val type: String,
    val coordinates: List<Double>
)
