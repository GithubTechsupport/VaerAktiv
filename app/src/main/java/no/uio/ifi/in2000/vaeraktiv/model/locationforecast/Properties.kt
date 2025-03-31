package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.Serializable


@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

