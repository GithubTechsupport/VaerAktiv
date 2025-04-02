package no.uio.ifi.in2000.vaeraktiv.model.ui

data class TodaysWeatherData(
    val tempNow : String, // NowCast
    val tempMax : String, // Location
    val tempMin : String, // Location
    val uv : String, // Location
    val wind : String, // NowCast
    val precipitationAmount : String, // NowCast
    val iconDesc : String, // Location
    val iconDescNow: String // NowCast
)
