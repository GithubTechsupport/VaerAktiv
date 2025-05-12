package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class Features (
    val geometry : Geometry,
    val properties: Properties
)