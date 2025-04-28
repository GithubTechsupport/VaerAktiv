package no.uio.ifi.in2000.vaeraktiv.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import javax.inject.Inject
import javax.inject.Named

class AiRepository @Inject constructor(@Named("OpenAi-Client") private val client: AiClient) {
    suspend fun getSuggestionsForOneDay(prompt: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions): SuggestedActivities? = withContext(Dispatchers.IO) {
        return@withContext try {
            client.getSuggestionsForOneDay(prompt, nearbyPlaces, routes)
        } catch (e: Exception) {
            throw e
        }
    }
}