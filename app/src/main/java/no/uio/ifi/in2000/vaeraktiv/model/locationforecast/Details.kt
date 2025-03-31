package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Details(
    @SerialName("air_pressure_at_sea_level")
    val airPressureAtSeaLevel: Double? = null,
    @SerialName("air_temperature")
    val airTemperature: Double? = null,
    @SerialName("air_temperature_max")
    val airTemperatureMax: Double? = null,
    @SerialName("air_temperature_min")
    val airTemperatureMin: Double? = null,
    @SerialName("cloud_area_fraction")
    val cloudAreaFraction: Double? = null,
    @SerialName("cloud_area_fraction_high")
    val cloudAreaFractionHigh: Double? = null,
    @SerialName("cloud_area_fraction_low")
    val cloudAreaFractionLow: Double? = null,
    @SerialName("cloud_area_fraction_medium")
    val cloudAreaFractionMedium: Double? = null,
    @SerialName("dew_point_temperature")
    val dewPointTemperature: Double? = null,
    @SerialName("fog_area_fraction")
    val fogAreaFraction: Double? = null,
    @SerialName("precipitation_amount")
    val precipitationAmount: Double? = null,
    @SerialName("relative_humidity")
    val relativeHumidity: Double? = null,
    @SerialName("ultraviolet_index_clear_sky")
    val ultravioletIndexClearSky: Double? = null,
    @SerialName("wind_from_direction")
    val windFromDirection: Double? = null,
    @SerialName("wind_speed")
    val windSpeed: Double? = null
)

