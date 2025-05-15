package no.uio.ifi.in2000.vaeraktiv.model.ai

import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units

/**
 * Holds a list of TimeSeries and units for constructing AI prompts.
 */
data class FormattedForecastDataForPrompt (
    val timeseries: List<TimeSeries>,
    val units: Units?,
    val location: String
) {
    override fun toString(): String {
        // Formats each time series entry into a prompt section
        val userPrompt = timeseries.joinToString("\n\n") {"""
            datetime: ${it.time}
            airTemperature: ${it.data.instant.details.airTemperature}
            precipitation: ${it.data.next1Hours?.details?.precipitationAmount}
            windSpeed: ${it.data.instant.details.windSpeed}
            cloudAreaFraction: ${it.data.instant.details.cloudAreaFraction}
            fogAreaFraction: ${it.data.instant.details.fogAreaFraction}
            ultravioletIndexClearSky: ${it.data.instant.details.ultravioletIndexClearSky}
            """.trimIndent()
        }
        return "The units are: $units\n\n$userPrompt\n\nUser's location is: $location"
    }
}
