package no.uio.ifi.in2000.vaeraktiv.model.loactionforecast

import kotlinx.serialization.Serializable

@Serializable
data class TimeSeries(
    val time: String,
    val data: Data
)

