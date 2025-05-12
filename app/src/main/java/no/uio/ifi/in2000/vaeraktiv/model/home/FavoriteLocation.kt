package no.uio.ifi.in2000.vaeraktiv.model.home

data class FavoriteLocation(
    val name: String,
    val iconDesc: String,
    val shortDesc: String,
    val highestTemp: String,
    val lowestTemp: String,
    val downPour: String,
    val uv: String,
    val wind: String,
    val lat: String,
    val lon: String
)
