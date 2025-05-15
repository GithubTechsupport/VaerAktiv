package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.Serializable

/**
 * Top-level forecast response from location forecast API.
 *
 * @param type response type identifier
 * @param geometry geospatial info of the forecast area
 * @param properties forecast metadata and time series data
 */
@Serializable
data class LocationForecastResponse(
    val type: String,
    val geometry: LGeometry,
    val properties: Properties
)
