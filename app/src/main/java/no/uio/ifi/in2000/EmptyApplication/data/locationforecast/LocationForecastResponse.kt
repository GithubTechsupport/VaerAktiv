package no.uio.ifi.in2000.EmptyApplication.data.locationforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationForecastResponse(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

@Serializable
data class Geometry(
    val type: String,
    val coordinates: List<Double>
)

@Serializable
data class Properties(
    val meta: Meta,
    val timeseries: List<TimeSeries>
)

@Serializable
data class Meta(
    @SerialName("updated_at")
    val updatedAt: String,
    val units: Units
)

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
)

@Serializable
data class TimeSeries(
    val time: String,
    val data: Data
)

@Serializable
data class Data(
    val instant: Instant,
    @SerialName("next_12_hours")
    val next12Hours: Forecast? = null,
    @SerialName("next_1_hours")
    val next1Hours: Forecast? = null,
    @SerialName("next_6_hours")
    val next6Hours: Forecast? = null
)

@Serializable
data class Instant(
    val details: Details
)

@Serializable
data class Forecast(
    val summary: Summary,
    val details: Details? = null
)

@Serializable
data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String
)

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
