package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val area: String? = null,
    @SerialName("awareness_type")
    val awarenessType: String? = null,
    val contact: String? = null,
    val description: String? = null,
    val eventAwarenessName: String? = null,
    val instruction: String? = null,
    val riskMatrixColor: String? = null
)