package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
