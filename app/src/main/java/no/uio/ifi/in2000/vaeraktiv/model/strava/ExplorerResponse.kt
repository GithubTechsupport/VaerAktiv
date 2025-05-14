package no.uio.ifi.in2000.vaeraktiv.model.strava

import kotlinx.serialization.Serializable

@Serializable
data class ExplorerResponse(
    val segments: List<ExplorerSegment>
)
