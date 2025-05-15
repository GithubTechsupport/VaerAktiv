package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

/**
 * Represents a single alert feature with geometry and detailed properties.
 *
 * @param geometry geospatial boundary of the alert area
 * @param properties descriptive attributes of the alert
 */
@Serializable
data class Features (
    val geometry : Geometry,
    val properties: Properties
)