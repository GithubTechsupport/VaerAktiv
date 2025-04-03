package no.uio.ifi.in2000.vaeraktiv.model.ui

data class ForecastToday(
    val tempNow : String? = null, // NowCast
    val tempMax : String? = null, // Location
    val tempMin : String? = null, // Location
    val uv : String? = null, // Location
    val windSpeed : String? = null, // NowCast
    val precipitationAmount : String? = null, // NowCast
    val icon : String? = null, // Location
    val iconNow : String? = null // NowCast
)
