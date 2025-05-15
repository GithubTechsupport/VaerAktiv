package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable

/**
 * A single timestamped forecast entry.
 *
 * @param time ISO datetime string of the forecast
 * @param data meteorological data at this time
 */
@Serializable
data class TimeSeries(
    val time: String,
    val data: Data
)

