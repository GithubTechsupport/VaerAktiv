package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Brief summary code for a forecast period.
 *
 * @param symbolCode code representing weather symbol (e.g., "clearsky_day")
 */
@Serializable
data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String
)

