package no.uio.ifi.in2000.vaeraktiv.model.ui

// Location på alt
data class ThisWeeksWeatherData(
    val date : String,
    val maxTemp : String,
    val uvMax : String, // burker ikke til å begynne med
    val iconDesc : String,
    val precipitationAmount : String, // bruker ikke til å begynne med
    val wind : String // bruker ikke til å begynne med.
)
