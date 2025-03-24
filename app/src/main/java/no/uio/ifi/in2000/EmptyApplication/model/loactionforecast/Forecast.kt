package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Details
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Summary

@Serializable
data class Forecast(
    val summary: Summary,
    val details: Details? = null
)
