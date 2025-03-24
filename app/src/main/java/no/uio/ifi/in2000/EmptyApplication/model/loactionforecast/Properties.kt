package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Meta
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.TimeSeries

@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

