package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Response model for sunrise/sunset times and metadata.
 *
 * @param copyright data provider attribution
 * @param licenseURL URL of the license for the data
 * @param type response type identifier
 * @param geometry geospatial metadata for the event
 * @param when time interval of the solar events
 * @param properties detailed solar event properties
 */
@Serializable
data class SunData(
    val copyright: String,
    val licenseURL: String,
    val type: String,
    val geometry: SunGeometry,
    val `when`: SunTimeInterval,
    val properties: SunProperties
)
