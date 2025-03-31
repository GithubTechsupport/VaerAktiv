package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("updated_at")
    val updatedAt: String,
    val units: Units
)
