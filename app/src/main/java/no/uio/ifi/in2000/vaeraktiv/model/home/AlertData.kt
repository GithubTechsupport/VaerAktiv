package no.uio.ifi.in2000.vaeraktiv.model.home

/**
 * Metadata for a meteorological alert.
 *
 * @param area affected geographic area
 * @param awarenessType severity level
 * @param description full alert description
 * @param eventAwarenessName event name (e.g., "Flood")
 * @param instruction recommended actions
 * @param riskMatrixColor risk color code
 * @param contact authority contact details
 */
data class AlertData(
    val area: String,
    val awarenessType: String? = null,
    val description: String? = null,
    val eventAwarenessName: String? = null,
    val instruction: String? = null,
    val riskMatrixColor: String? = null,
    val contact : String? = null
)
