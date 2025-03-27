package no.uio.ifi.in2000.vaeraktiv.model.Ai

import no.uio.ifi.in2000.vaeraktiv.model.loactionforecast.Properties

data class Prompt(
    val properties: Properties,
    val limit: Int
) {
    override fun toString(): String {
        val userPrompt = properties.timeseries.take(limit).joinToString("\n\n") { ts ->
            """
            datetime: ${ts.time}
            temperature: ${ts.data.instant.details.airTemperature}
            precipitation: ${ts.data.instant.details.precipitationAmount}
            """.trimIndent()
        }
        return userPrompt
    }
}