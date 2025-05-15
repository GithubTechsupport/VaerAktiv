package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Metadata for forecast, including timestamp and units mapping.
 *
 * @param updatedAt ISO timestamp when data was last refreshed
 * @param units units for each measured parameter
 */
@Serializable
data class Meta(
    @SerialName("updated_at")
    val updatedAt: String,
    val units: Units
)
