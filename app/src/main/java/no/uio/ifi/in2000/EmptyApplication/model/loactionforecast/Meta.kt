package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Units

@Serializable
data class Meta(
    @SerialName("updated_at")
    val updatedAt: String,
    val units: Units
)
