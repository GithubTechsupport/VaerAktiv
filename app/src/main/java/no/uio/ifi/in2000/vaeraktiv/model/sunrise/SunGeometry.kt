package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Geospatial metadata for solar events.
 *
 * @param type GeoJSON type (e.g., "Point")
 * @param coordinates [longitude, latitude] list
 */
@Serializable
data class SunGeometry(
    val type: String,
    val coordinates: List<Double>
)
