package no.uio.ifi.in2000.vaeraktiv.model.loactionforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String
)

