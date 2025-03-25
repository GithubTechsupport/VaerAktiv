package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable


@Serializable
data class Forecast(
    val summary: Summary,
    val details: Details? = null
)
