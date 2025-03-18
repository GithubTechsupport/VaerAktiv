package no.uio.ifi.in2000.EmptyApplication.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val area : String,
    val awareness_level : String,
    val eventAwarnessName : String
)





