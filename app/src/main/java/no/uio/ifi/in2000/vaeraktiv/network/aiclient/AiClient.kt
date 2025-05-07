package no.uio.ifi.in2000.vaeraktiv.network.aiclient

import android.util.Log
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatResponseFormat
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


abstract class AiClient {
    val prompt = Prompt()
    abstract suspend fun getSuggestionsForOneDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String): String?
    abstract suspend fun getSingleSuggestionForDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String): String?
}

class OpenAiClientWrapper @Inject constructor(private val client: OpenAI) : AiClient() {
    private val model = ModelId("gpt-4.1")

    override suspend fun getSuggestionsForOneDay(
        forecastData: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String): String? = withContext(Dispatchers.IO) {
        val absolutePrompt = "${prompt.fullPrompt}${exclusion}${preferences}WEATHERFORECAST START:\n\n<<<\n$forecastData\n>>>\n\nWEATHERFORECAST END\n\nNEARBY PLACES START:\n\n<<<\n$nearbyPlaces\n>>>\n\nNEARBY PLACES END\n\nNEARBY ROUTES START:\n\n<<<\n$routes\n>>>\n\nNEARBY ROUTES END"
        Log.d("Prompt", absolutePrompt)
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

    override suspend fun getSingleSuggestionForDay(
        forecastData: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String): String? = withContext(Dispatchers.IO) {
        val absolutePrompt = "${prompt.fullPromptSingular}$exclusion${preferences}WEATHERFORECAST START:\n\n<<<\n$forecastData\n>>>\n\nWEATHERFORECAST END\n\nNEARBY PLACES START:\n\n<<<\n$nearbyPlaces\n>>>\n\nNEARBY PLACES END\n\nNEARBY ROUTES START:\n\n<<<\n$routes\n>>>\n\nNEARBY ROUTES END"
        Log.d("Prompt", absolutePrompt)
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
        val suggestionsStart = System.currentTimeMillis()
        val response: String? = client.chatCompletion(request).choices.firstOrNull()?.message?.content
        val suggestionsEnd = System.currentTimeMillis()
        Log.d("Timing", "getSingleSuggestionForDay: ${suggestionsEnd - suggestionsStart} ms")
        return@withContext response
    }

}

@Module
@InstallIn(SingletonComponent::class)
object AiClientModule {

    @Provides
    @Singleton
    @Named("OpenAi-Client")
    fun provideOpenAiClient(): AiClient {
        val apiKey = "3yoURlTha7POk9V41F9wbifVAKkkrvpEEvfsFZZvBHhLgii2QhXPJQQJ99BDACfhMk5XJ3w3AAABACOG6HGy"
        val azureHost = OpenAIHost.azure(
            resourceName = "UIO-MN-IFI-IN2000-SWE1",
            deploymentId = "gpt-4.1-T31",
            apiVersion = "2024-12-01-preview"
        )
        val client = OpenAI(
            OpenAIConfig(
                timeout = Timeout(socket = 120.seconds),
                token = apiKey,
                host = azureHost
            )
        )
        return OpenAiClientWrapper(client)
    }
}