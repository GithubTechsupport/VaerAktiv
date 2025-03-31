package no.uio.ifi.in2000.vaeraktiv.model.nowcast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Units(
    @SerialName("air_temperature")
    val airTemperature: String,
    @SerialName("precipitation_amount")
    val precipitationAmount: String,
    @SerialName("precipitation_rate")
    val precipitationRate: String,
    @SerialName("relative_humidity")
    val relativeHumidity: String,
    @SerialName("wind_from_direction")
    val windFromDirection: String,
    @SerialName("wind_speed")
    val windSpeed: String,
    @SerialName("wind_speed_of_gust")
    val windSpeedOfGust: String
)