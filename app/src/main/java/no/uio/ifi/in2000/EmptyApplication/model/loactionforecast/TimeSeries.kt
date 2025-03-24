package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Data

@Serializable
data class TimeSeries(
    val time: String,
    val data: Data
)

