package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("geometry")
data class LGeometry(
    val type: String,
    val coordinates: List<Double>
)
