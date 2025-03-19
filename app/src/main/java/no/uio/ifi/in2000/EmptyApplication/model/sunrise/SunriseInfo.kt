package no.uio.ifi.in2000.EmptyApplication.model.sunrise
import kotlinx.serialization.Serializable


@Serializable
data class SunData(
    val copyright: String,
    val licenseURL: String,
    val type: String,
    val geometry: Geometry,
    val `when`: TimeInterval,
    val properties: Properties
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

@Serializable
data class TimeInterval(
    val interval: List<String>
)

@Serializable
data class Properties(
    val body: String,
    val sunrise: SunEvent,
    val sunset: SunEvent,
    val solarnoon: SolarEvent,
    val solarmidnight: SolarEvent
)

@Serializable
data class SunEvent(
    val time: String,
    val azimuth: Double
)

@Serializable
data class SolarEvent(
    val time: String,
    val discCentrEelevation: Double,
    val visible: Boolean
)

