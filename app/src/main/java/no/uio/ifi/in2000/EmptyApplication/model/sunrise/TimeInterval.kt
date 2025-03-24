package no.uio.ifi.in2000.EmptyApplication.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class TimeInterval(
    val interval: List<String>
)
