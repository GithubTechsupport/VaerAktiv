package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SolarEvent(
    val time: String,
    val visible: Boolean
)
