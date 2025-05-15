package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single Strava segment/route.
 *
 * @param id unique segment identifier
 * @param name segment name or title
 * @param distance total length of the segment in meters
 * @param averageGrade average incline percentage
 * @param elevationGain total elevation gain in meters
 * @param polyline encoded route polyline
 * @param startPosition [latitude, longitude] of segment start
 * @param endPosition [latitude, longitude] of segment end
 */
@Serializable
data class ExplorerSegment(
    val id: Long,
    val name: String,
    val distance: Double,
    @SerialName("avg_grade") val averageGrade: Double,
    @SerialName("elev_difference") val elevationGain: Double,
    @SerialName("points") val polyline: String,
    @SerialName("start_latlng") val startPosition: List<Double>,
    @SerialName("end_latlng") val endPosition: List<Double>
)
