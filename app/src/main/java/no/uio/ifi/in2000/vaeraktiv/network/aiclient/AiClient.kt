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
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.BuildConfig
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
//import org.oremif.deepseek.api.chat
//import org.oremif.deepseek.client.DeepSeekClient
//import org.oremif.deepseek.models.ChatCompletionParams
//import org.oremif.deepseek.models.ChatModel
//import org.oremif.deepseek.models.ResponseFormat
//import org.oremif.deepseek.models.chatCompletionParams
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds


abstract class AiClient {
    val prompt = Prompt()
    abstract suspend fun getSuggestionsForEveryDay(forecastData: FormattedForecastDataForPrompt): SuggestedActivities?
    abstract suspend fun getSuggestionsForOneDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions): SuggestedActivities?
}

//class DeepseekClientWrapper @Inject constructor(private val client: DeepSeekClient) : AiClient() {
//    private val params : ChatCompletionParams = chatCompletionParams {
//        model = ChatModel.DEEPSEEK_CHAT
//        temperature = 0.5
//        responseFormat = ResponseFormat.jsonObject
//    }
//
//    override suspend fun getResponse(prompt: Prompt): JsonResponse? = withContext(Dispatchers.IO) {
//        val response: org.oremif.deepseek.models.ChatCompletion = client.chat(params) {
//            system(systemPrompt)
//            user("$examplesPrompt\n\nFollowing is the user prompt:\n\n<<<\n$prompt\n>>>")
//        }
//        val parsedResponse = response.choices.firstOrNull()?.message?.content?.let {
//            Json.decodeFromString<JsonResponse>(it)
//        }
//        return@withContext parsedResponse
//    }
//
//}

class OpenAiClientWrapper @Inject constructor(private val client: OpenAI) : AiClient() {
    override suspend fun getSuggestionsForEveryDay(forecastData: FormattedForecastDataForPrompt): SuggestedActivities? = withContext(Dispatchers.IO) {
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = prompt.systemPrompt),
            ChatMessage(role = ChatRole.User, content = "${prompt.fullPrompt}\n\nFollowing is the user prompt:\n\n<<<\n$forecastData\n>>>")
        )
        val request = ChatCompletionRequest (
            model = ModelId("gpt-4o"),
            messages = messages,
            temperature = 0.5,
            responseFormat = ChatResponseFormat.JsonObject
        )
        val response: com.aallam.openai.api.chat.ChatCompletion = client.chatCompletion(request)
        val parsedResponse = response.choices.firstOrNull()?.message?.content?.let {
            Json.decodeFromString<SuggestedActivities>(it)
        }
        return@withContext parsedResponse
    }

    override suspend fun getSuggestionsForOneDay(forecastData: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions): SuggestedActivities? = withContext(Dispatchers.IO) {
        Log.d("OpenAiClientWrapper", "getSuggestionsForOneDay: $forecastData")
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = prompt.systemPrompt),
            ChatMessage(role = ChatRole.User, content = "${prompt.fullPrompt}\n\nFollowing is the user prompt:\n\n<<<\n$forecastData\n>>>\n\nFollowing is nearby places data:\n\n<<<\n$nearbyPlaces\n>>>\n\nFollowing is the nearby routes data:\n\n<<<\n$routes\n>>>")
        )
        val request = ChatCompletionRequest (
            model = ModelId("gpt-4o"),
            messages = messages,
            temperature = prompt.temperature,
            responseFormat = ChatResponseFormat.JsonObject
        )
        val response: com.aallam.openai.api.chat.ChatCompletion = client.chatCompletion(request)
        val parsedResponse = response.choices.firstOrNull()?.message?.content?.let {
            Json.decodeFromString<SuggestedActivities>(it)
        }
        return@withContext parsedResponse
    }

}

@Module
@InstallIn(SingletonComponent::class)
object AiClientModule {

//    @Provides
//    @Singleton
//    @Named("Deepseek-Client")
//    fun provideDeepseekClient(): AiClient {
//        val apiKey = ""
//        val client = DeepSeekClient(apiKey) {
//            chatCompletionTimeout(120_000)
//        }
//        return DeepseekClientWrapper(client)
//    }

    @Provides
    @Singleton
    @Named("OpenAi-Client")
    fun provideOpenAiClient(): AiClient {
        val apiKey = BuildConfig.OPENAI_API_KEY
        val azureHost = OpenAIHost.azure(
            resourceName = "UIO-MN-IFI-IN2000-SWE1",
            deploymentId = "gpt-4o-t31",
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