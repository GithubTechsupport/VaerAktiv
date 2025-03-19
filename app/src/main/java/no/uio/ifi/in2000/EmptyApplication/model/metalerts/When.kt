package no.uio.ifi.in2000.EmptyApplication.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class When(
    val interval: List<String>
)
