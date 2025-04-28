package no.uio.ifi.in2000.vaeraktiv.network.aiclient

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
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
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
    val systemPrompt = """The user will provide a weather forecast. Your task is to select 3 different time intervals per day and suggest one physical activity for each interval, based on the weather conditions, the user's location, and any other requirements defined in the user prompt."""

    val examplesPrompt = """
            All output should be written in Norwegian, except for the keys in the JSON output, which should be in English, as described in the examples below.
            
            Based on the weather forecast and the user's location, choose 3 different time intervals for each day and suggest one physical activity for each interval for the next 7 days.
            In total, there should be a maximum of 21 different activities, spread across 7 days and 3 time intervals per day.
            
            All activities must be physical and involve movement, either indoors or outdoors. Examples of physical activities include: hiking, cycling, swimming, climbing, bowling, squash, dancing, yoga, trampoline park, gym workout, ice skating, skiing, frisbee golf, obstacle course, etc.
            Suggestions such as "going to a café," "cinema," "museum," or similar should NOT be suggested, as these are not physical activities.
            
            Activities must be realistic and available near the user's location. They should be adapted to the weather conditions: On days with rain or low temperatures (9°C or lower), more indoor activities should be suggested, but outdoor activities can also be included if suitable (e.g., fishing in the rain, ice skating, etc.).
            
            The "activity" field should contain a brief description of the activity (e.g., "Hiking along Akerselva").
            The "activityDesc" field should provide a more detailed description and explain where and how the activity can be carried out.
            
            The example input and output below are shortened versions of what you will actually produce.
            
            BEGINNING OF EXAMPLES
            
            EXAMPLE INPUT:
            
            WEATHERFORECAST START
            
            datetime: 2025-06-24T12:00:00Z
            temperature: 13.0
            precipitation: 0.0
            
            datetime: 2025-06-24T13:00:00Z
            temperature: 17.0
            precipitation: 0.0
            
            datetime: 2025-06-24T14:00:00Z
            temperature: 22.0
            precipitation: 0.0
            
            datetime: 2025-06-24T15:00:00Z
            temperature: 20.0
            precipitation: 0.0
            
            datetime: 2025-06-24T16:00:00Z
            temperature: 19.0
            precipitation: 0.0
            
            datetime: 2025-06-25T12:00:00Z
            temperature: 9.0
            precipitation: 0.0
            
            datetime: 2025-06-25T13:00:00Z
            temperature: 10.0
            precipitation: 0.0
            
            datetime: 2025-06-25T14:00:00Z
            temperature: 12.0
            precipitation: 0.0
            
            datetime: 2025-06-25T15:00:00Z
            temperature: 10.0
            precipitation: 0.0
            
            datetime: 2025-06-25T16:00:00Z
            temperature: 10.0
            precipitation: 0.0
            
            WEATHERFORECAST END
            
            USER'S LOCATION: Storgata
            
            EXAMPLE JSON OUTPUT:
            {
                "activities": [
                    {
                        "month": "6",
                        "dayOfMonth": "24",
                        "timeStart": "12:00",
                        "timeEnd": "14:00",
                        "activity": "Hiking along Akerselva",
                        "activityDesc": "The weather is dry and the temperature is comfortable. Start at Storgata and walk along the Akerselva river towards Grünerløkka. This route offers fresh air and a scenic walk."
                    },
                    {
                        "month": "6",
                        "dayOfMonth": "24",
                        "timeStart": "14:00",
                        "timeEnd": "16:00",
                        "activity": "Cycling to Sognsvann",
                        "activityDesc": "It’s warm and sunny. Rent a city bike and cycle from downtown to Sognsvann for an active and nature-filled experience."
                    },
                    {
                        "month": "6",
                        "dayOfMonth": "24",
                        "timeStart": "16:00",
                        "timeEnd": "18:00",
                        "activity": "Frisbee golf in Torshovparken",
                        "activityDesc": "The temperature is still high. Play frisbee golf in Torshovparken – a fun and social activity suitable for all ages."
                    },
                    {
                        "month": "6",
                        "dayOfMonth": "25",
                        "timeStart": "12:00",
                        "timeEnd": "14:00",
                        "activity": "Indoor climbing at Oslo Klatresenter",
                        "activityDesc": "It’s chilly outside, so try indoor bouldering or rope climbing at Oslo Klatresenter in Torshov. Suitable for both beginners and experienced climbers."
                    },
                    {
                        "month": "6",
                        "dayOfMonth": "25",
                        "timeStart": "14:00",
                        "timeEnd": "16:00",
                        "activity": "Squash at Bislett Squash",
                        "activityDesc": "With low temperatures, indoor squash is a great way to get active. Bislett Squash offers court rentals and equipment."
                    },
                    {
                        "month": "6",
                        "dayOfMonth": "25",
                        "timeStart": "16:00",
                        "timeEnd": "18:00",
                        "activity": "Gym workout at SATS Storo",
                        "activityDesc": "End the day with a workout at the gym. SATS Storo offers both strength and cardio training indoors."
                    }
                ]
            }
            
            END OF EXAMPLES
            """.trimIndent()
    abstract suspend fun getResponse(prompt: Prompt): JsonResponse?
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
    override suspend fun getResponse(prompt: Prompt): JsonResponse? = withContext(Dispatchers.IO) {
        val messages = listOf(
            ChatMessage(role = ChatRole.System, content = systemPrompt),
            ChatMessage(role = ChatRole.User, content = "$examplesPrompt\n\nFollowing is the user prompt:\n\n<<<\n$prompt\n>>>")
        )
        val request = ChatCompletionRequest (
            model = ModelId("gpt-4o"),
            messages = messages,
            temperature = 0.5,
            responseFormat = ChatResponseFormat.JsonObject
        )
        val response: com.aallam.openai.api.chat.ChatCompletion = client.chatCompletion(request)
        val parsedResponse = response.choices.firstOrNull()?.message?.content?.let {
            Json.decodeFromString<JsonResponse>(it)
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