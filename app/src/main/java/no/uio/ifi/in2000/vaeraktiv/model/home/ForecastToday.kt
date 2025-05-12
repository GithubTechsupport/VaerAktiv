package no.uio.ifi.in2000.vaeraktiv.model.home

data class ForecastToday(
    val tempNow : String? = null,
    val tempMax : String? = null,
    val tempMin : String? = null,
    val uv : String? = null,
    val windSpeed : String? = null,
    val precipitationAmount : String? = null,
    val icon : String? = null,
    val iconNow : String? = null
)
