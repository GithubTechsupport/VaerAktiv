package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable


@Serializable
data class LocationForecastResponse(
    val type: String,
    val geometry: LGeometry,
    val properties: Properties
)

