package no.uio.ifi.in2000.vaeraktiv.model.aggregateModels

/**
 * Represents a geographic location with name and coordinates.
 *
 * @param addressName display name of the location
 * @param lat latitude as string
 * @param lon longitude as string
 */
data class Location(
    val addressName: String,
    val lat: String,
    val lon: String
)
