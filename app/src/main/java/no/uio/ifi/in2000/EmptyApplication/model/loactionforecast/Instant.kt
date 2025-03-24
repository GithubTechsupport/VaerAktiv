package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.Serializable


@Serializable
data class Instant(
    val details: Details
)
