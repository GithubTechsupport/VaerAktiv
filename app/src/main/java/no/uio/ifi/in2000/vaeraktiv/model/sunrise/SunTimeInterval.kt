package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SunTimeInterval(
    val interval: List<String>
)
