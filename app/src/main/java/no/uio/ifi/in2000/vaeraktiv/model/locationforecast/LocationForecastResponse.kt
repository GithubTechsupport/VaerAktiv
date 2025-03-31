package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.Serializable


@Serializable
data class LocationForecastResponse(
    val type: String,
    val geometry: LGeometry,
    val properties: Properties
)

