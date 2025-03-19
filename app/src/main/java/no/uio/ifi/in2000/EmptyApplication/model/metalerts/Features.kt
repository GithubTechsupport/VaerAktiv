package no.uio.ifi.in2000.EmptyApplication.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class Features (
    val geometry : Geometry,
    val properties: Properties,
    val type: String,
    val `when`: When
)