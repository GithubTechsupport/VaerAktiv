package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

/**
 * Wrapper for list of alert features returned by met alerts API.
 */
@Serializable
data class FeaturesResponse(
    val features: List<Features>,
    val type: String? = null
)
