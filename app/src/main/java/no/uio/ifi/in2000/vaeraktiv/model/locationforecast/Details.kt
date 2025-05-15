package no.uio.ifi.in2000.vaeraktiv.model.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Detailed meteorological parameters at an instant.
 *
 * @param airPressureAtSeaLevel pressure at sea level (hPa)
 * @param airTemperature air temperature (°C)
 * @param airTemperatureMax max air temperature (°C)
 * @param airTemperatureMin min air temperature (°C)
 * @param cloudAreaFraction total cloud cover percentage
 * @param cloudAreaFractionHigh high-level cloud cover percentage
 * @param cloudAreaFractionLow low-level cloud cover percentage
 * @param cloudAreaFractionMedium medium-level cloud cover percentage
 * @param dewPointTemperature dew point temperature (°C)
 * @param fogAreaFraction fog cover percentage
 * @param precipitationAmount precipitation amount (mm)
 * @param relativeHumidity relative humidity (%)
 * @param ultravioletIndexClearSky UV index (clear sky)
 * @param windFromDirection wind direction (degrees)
 * @param windSpeed wind speed (m/s)
 */
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

