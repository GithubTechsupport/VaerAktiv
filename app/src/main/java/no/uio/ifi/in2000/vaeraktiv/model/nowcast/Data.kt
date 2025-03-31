package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val instant: Instant,
    @SerialName("next_1_hours")
    val next1Hours: Forecast? = null,
)
