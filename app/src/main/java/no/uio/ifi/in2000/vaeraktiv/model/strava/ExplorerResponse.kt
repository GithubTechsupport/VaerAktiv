package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.Serializable

/**
 * Response from Strava Explorer API containing a list of segments.
 *
 * @param segments list of popular route segments
 */
@Serializable
data class ExplorerResponse(
    val segments: List<ExplorerSegment>
)
