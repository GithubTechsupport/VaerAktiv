package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable

/**
 * Holds forecast metadata and a series of time-stamped data points.
 *
 * @param meta metadata including units and update time
 * @param timeseries list of time-based forecast entries
 */
@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

