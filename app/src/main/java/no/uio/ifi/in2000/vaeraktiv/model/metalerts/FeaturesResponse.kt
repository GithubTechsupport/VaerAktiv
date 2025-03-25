package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class FeaturesResponse(
    val features: List<Features>,
    val type : String? = null
)