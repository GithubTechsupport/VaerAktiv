package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Geometry
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Properties

@Serializable
data class LocationForecastResponse(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

