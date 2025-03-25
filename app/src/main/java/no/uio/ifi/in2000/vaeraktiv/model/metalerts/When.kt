package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class When(
    val interval: List<String>
)
