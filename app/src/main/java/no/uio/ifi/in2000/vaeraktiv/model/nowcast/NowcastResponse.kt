package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable

@Serializable
data class NowcastResponse(
    val type: String,
    val geometry: LGeometry,
    val properties: Properties
)