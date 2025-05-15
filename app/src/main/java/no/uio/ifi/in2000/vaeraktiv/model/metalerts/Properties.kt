package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Alert properties describing a meteorological warning.
 *
 * @param area affected geographic area name
 * @param awarenessType severity level or alert type
 * @param contact contact information for authorities
 * @param description detailed alert description
 * @param eventAwarenessName name of the weather event
 * @param instruction recommended actions for the public
 * @param riskMatrixColor color code indicating risk severity
 */
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