package no.uio.ifi.in2000.vaeraktiv.network.aiclient

import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatResponseFormat
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import javax.inject.Inject

/**
 * Wrapper around OpenAI client to fetch chat-based activity suggestions.
 */
class OpenAiClientWrapper @Inject constructor(private val client: OpenAI) : AiClient() {
    private val model = ModelId("gpt-4.1")

    /**
     * Requests JSON-formatted suggestions for a full day prompt.
     *
     * @param forecastData The formatted forecast data for the prompt.
     * @param nearbyPlaces The nearby places suggestions.
     * @param routes The routes suggestions.
     * @param preferences User preferences for the suggestions.
     * @param exclusion Exclusion criteria for the suggestions.
     */
    override suspend fun getSuggestionsForOneDay(
        forecastData: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String): String? = withContext(Dispatchers.IO) {
        val absolutePrompt = "${prompt.fullPrompt}${exclusion}${preferences}WEATHERFORECAST START:\n\n<<<\n$forecastData\n>>>\n\nWEATHERFORECAST END\n\nNEARBY PLACES START:\n\n<<<\n$nearbyPlaces\n>>>\n\nNEARBY PLACES END\n\nNEARBY ROUTES START:\n\n<<<\n$routes\n>>>\n\nNEARBY ROUTES END"
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = prompt.systemPrompt),
            ChatMessage(role = ChatRole.User, content = absolutePrompt)
        )
        val request = ChatCompletionRequest (
            model = model,
            messages = messages,
            temperature = prompt.temperature,
            responseFormat = ChatResponseFormat.JsonObject
        )
        val response: String? = client.chatCompletion(request).choices.firstOrNull()?.message?.content
        return@withContext response
    }

    /**
     * Requests a single activity suggestion for a day.
     *
     * @param forecastData The formatted forecast data for the prompt.
     * @param nearbyPlaces The nearby places suggestions.
     * @param routes The routes suggestions.
     * @param preferences User preferences for the suggestions.
     * @param exclusion Exclusion criteria for the suggestions.
     *
     */
    override suspend fun getSingleSuggestionForDay(
        forecastData: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String): String? = withContext(Dispatchers.IO) {
        val absolutePrompt = "${prompt.fullPromptSingular}$exclusion${preferences}WEATHERFORECAST START:\n\n<<<\n$forecastData\n>>>\n\nWEATHERFORECAST END\n\nNEARBY PLACES START:\n\n<<<\n$nearbyPlaces\n>>>\n\nNEARBY PLACES END\n\nNEARBY ROUTES START:\n\n<<<\n$routes\n>>>\n\nNEARBY ROUTES END"
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = prompt.systemPromptSingular),
            ChatMessage(role = ChatRole.User, content = absolutePrompt)
        )
        val request = ChatCompletionRequest (
            model = model,
            messages = messages,
            temperature = prompt.temperature,
            responseFormat = ChatResponseFormat.JsonObject
        )
        val response: String? = client.chatCompletion(request).choices.firstOrNull()?.message?.content
        return@withContext response
    }

}
