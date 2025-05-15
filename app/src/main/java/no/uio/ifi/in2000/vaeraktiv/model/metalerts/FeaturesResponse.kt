package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable


/**
 * Container for a list of meteorological alert features.
 *
 * @param features list of feature objects with geometry and properties
 * @param type optional response type identifier
 */
@Serializable
data class FeaturesResponse(
    val features: List<Features>,
    val type: String? = null
)
