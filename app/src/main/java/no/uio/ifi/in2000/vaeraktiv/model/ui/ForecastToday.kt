package no.uio.ifi.in2000.vaeraktiv.model.ui

data class ForecastToday(
    val tempNow : String, // NowCast
    val tempMax : String, // Location
    val tempMin : String, // Location
    val uv : String, // Location
    val windSpeed : String, // NowCast
    val precipitationAmount : String, // NowCast
    val icon : String, // Location
    val iconNow: String // NowCast
)
