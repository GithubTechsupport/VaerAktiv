package no.uio.ifi.in2000.vaeraktiv.network.aiclient

import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions


abstract class AiClient {
    val prompt = Prompt()
    abstract suspend fun getSuggestionsForOneDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String): String?
    abstract suspend fun getSingleSuggestionForDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String): String?
}
