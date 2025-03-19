package no.uio.ifi.in2000.EmptyApplication.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class Resource(
    val description: String? = null,
    val mimeType: String? = null,
    val uri: String? = null
)
