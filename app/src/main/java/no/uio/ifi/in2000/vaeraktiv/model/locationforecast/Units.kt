package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Units(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: String,
    @SerialName("air_temperature")
    val airTemperature: String,
    @SerialName("air_temperature_max")
    val airTemperatureMax: String,
    @SerialName("air_temperature_min")
    val airTemperatureMin: String,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: String,
    @SerialName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: String,
    @SerialName("cloud_area_fraction_low")
    val cloudAreaFractionLow: String,
    @SerialName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: String,
    @SerialName("dew_point_temperature")
    val dewPointTemperature: String,
    @SerialName("fog_area_fraction")
    val fogAreaFraction: String,
    @SerialName("precipitation_amount")
    val precipitationAmount: String,
    @SerialName("relative_humidity")
    val relativeHumidity: String,
    @SerialName("ultraviolet_index_clear_sky")
    val ultravioletIndexClearSky: String,
    @SerialName("wind_from_direction")
    val windFromDirection: String,
    @SerialName("wind_speed")
    val windSpeed: String
) {
    override fun toString(): String {
        return "airTemperature: $airTemperature, precipitationAmount: $precipitationAmount, windSpeed: $windSpeed, cloudAreaFraction: $cloudAreaFraction, fogAreaFraction: $fogAreaFraction, ultravioletIndexClearSky: $ultravioletIndexClearSky"
    }
}