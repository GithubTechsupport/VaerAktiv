package no.uio.ifi.in2000.EmptyApplication.model.sunrise

import kotlinx.serialization.Serializable

@Serializable
data class SunData(
    val copyright: String,
    val licenseURL: String,
    val type: String,
    val geometry: SunGeometry,
    val `when`: SunTimeInterval,
    val properties: SunProperties
)
