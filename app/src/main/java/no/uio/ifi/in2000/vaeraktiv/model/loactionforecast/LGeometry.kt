package no.uio.ifi.in2000.vaeraktiv.model.loactionforecast

import kotlinx.serialization.Serializable

@Serializable
data class LGeometry(
    val type: String,
    val coordinates: List<Double>
)
