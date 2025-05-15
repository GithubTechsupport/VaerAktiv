package no.uio.ifi.in2000.vaeraktiv.model.sunrise

import kotlinx.serialization.Serializable

/**
 * Holds sunrise/sunset interval timestamps.
 *
 * @param interval list of ISO strings [start, end]
 */
@Serializable
data class SunTimeInterval(
    val interval: List<String>
)
