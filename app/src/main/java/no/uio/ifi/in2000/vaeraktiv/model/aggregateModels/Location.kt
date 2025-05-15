package no.uio.ifi.in2000.vaeraktiv.model.aggregateModels

/**
 * Represents a geographic location with an address and coordinates.
 */
data class Location(
    val addressName: String,
    val lat: String,
    val lon: String
)
