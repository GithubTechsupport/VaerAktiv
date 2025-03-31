package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Details(
    @SerialName("air_temperature")
    val airTemperature: Double? = null,
    @SerialName("precipitation_rate")
    val precipitationRate: Double? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: Double? = null,
    @SerialName("relative_humidity")
    val relativeHumidity: Double? = null,
    @SerialName("wind_from_direction")
    val windFromDirection: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null,
    @SerialName("wind_speed_of_gust")
    val windSpeedOfGust: Double? = null
)

