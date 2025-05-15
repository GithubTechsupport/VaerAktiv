package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable

/**
 * Top-level forecast response from nowcast API.
 *
 * @param type response type identifier
 * @param geometry geospatial info of the forecast area
 * @param properties forecast metadata and time series data
 */
@Serializable
data class NowcastResponse(
    val type: String,
    val geometry: LGeometry,
    val properties: Properties
)
