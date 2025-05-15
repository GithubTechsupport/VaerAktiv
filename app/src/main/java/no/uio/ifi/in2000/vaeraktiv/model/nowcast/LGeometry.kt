package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Geospatial geometry with coordinate points.
 *
 * @param type geometry type (e.g., "Point")
 * @param coordinates list of coordinates [longitude, latitude, elevation]
 */
@Serializable
@SerialName("geometry")
data class LGeometry(
    val type: String,
    val coordinates: List<Double>
)
