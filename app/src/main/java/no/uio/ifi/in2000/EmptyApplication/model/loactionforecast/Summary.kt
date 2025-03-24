package no.uio.ifi.in2000.EmptyApplication.model.loactionforecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("symbol_code")
    val symbolCode: String
)

