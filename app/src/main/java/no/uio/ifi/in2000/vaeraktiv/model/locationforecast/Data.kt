package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val instant: Instant,
    @SerialName("next_12_hours")
    val next12Hours: Forecast? = null,
    @SerialName("next_1_hours")
    val next1Hours: Forecast? = null,
    @SerialName("next_6_hours")
    val next6Hours: Forecast? = null
)
