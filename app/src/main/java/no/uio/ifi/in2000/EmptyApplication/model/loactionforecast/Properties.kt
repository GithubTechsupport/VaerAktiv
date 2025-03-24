package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable


@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

