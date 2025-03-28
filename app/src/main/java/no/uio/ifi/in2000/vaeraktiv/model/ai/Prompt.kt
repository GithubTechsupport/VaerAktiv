package no.uio.ifi.in2000.vaeraktiv.model.ai

import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Properties

data class Prompt(
    val properties: Properties,
    val limit: Int
) {
    override fun toString(): String {
        val userPrompt = properties.timeseries.joinToString("\n\n") {
            """
            datetime: ${it.time}
            temperature: ${it.data.instant.details.airTemperature}
            precipitation: ${it.data.instant.details.precipitationAmount}
            """.trimIndent()
        }
        return userPrompt
    }
}