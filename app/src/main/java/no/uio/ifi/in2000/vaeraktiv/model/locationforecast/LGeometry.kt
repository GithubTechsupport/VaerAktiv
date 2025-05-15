package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.Serializable

/**
 * Geospatial geometry with coordinate points.
 *
 * @param type geometry type (e.g., "Point")
 * @param coordinates list of coordinates [longitude, latitude, elevation]
 */
@Serializable
data class LGeometry(
    val type: String,
    val coordinates: List<Double>
)
