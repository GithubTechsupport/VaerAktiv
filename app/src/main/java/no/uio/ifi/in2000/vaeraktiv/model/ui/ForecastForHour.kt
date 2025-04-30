package no.uio.ifi.in2000.vaeraktiv.model.ui

data class ForecastForHour(
    val temp : String? = null,
    val windSpeed : String? = null,
    val precipitationAmount : String? = null,
    val icon : String? = null,
    val time : String? = null,
    val uv : String? = null
)
