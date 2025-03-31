package no.uio.ifi.in2000.vaeraktiv.model.ui

data class TodaysWeatherData(
    val tempNow : String,
    val tempMax : String,
    val uv : String,
    val wind : String,
    val precipitationAmount : String,
    val iconDesc : String
)
