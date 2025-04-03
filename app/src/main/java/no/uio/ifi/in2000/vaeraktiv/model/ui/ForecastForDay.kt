package no.uio.ifi.in2000.vaeraktiv.model.ui

// Location på alt
data class ForecastForDay(
    val date : String,
    val maxTemp : String,
    //val uv : String, // burker ikke til å begynne med
    val icon : String,
    //val precipitationAmount : String, // bruker ikke til å begynne med
    //val wind : String // bruker ikke til å begynne med.
)
