package no.uio.ifi.in2000.vaeraktiv.model.loactionforecast

import kotlinx.serialization.Serializable


@Serializable
data class Forecast(
    val summary: Summary,
    val details: Details? = null
)
