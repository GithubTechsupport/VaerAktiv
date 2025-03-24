package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Forecast
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Instant

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
