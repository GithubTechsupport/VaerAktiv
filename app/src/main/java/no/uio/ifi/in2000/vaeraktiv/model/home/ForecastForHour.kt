package no.uio.ifi.in2000.vaeraktiv.model.home

data class ForecastForHour(
    val time : String? = null,
    val temp : String? = null,
    val windSpeed : String? = null,
    val precipitationAmount : String? = null,
    val icon : String? = null,
    val uv : String? = null
)
