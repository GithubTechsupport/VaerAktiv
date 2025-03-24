package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable
import no.uio.ifi.in2000.EmptyApplication.data.locationforecast.Details

@Serializable
data class Instant(
    val details: Details
)
