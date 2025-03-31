package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.Serializable


@Serializable
data class Instant(
    val details: Details
)
