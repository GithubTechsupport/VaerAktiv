package no.uio.ifi.in2000.vaeraktiv.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import org.oremif.deepseek.api.chat
import org.oremif.deepseek.client.DeepSeekClient
import org.oremif.deepseek.models.ChatCompletion
import org.oremif.deepseek.models.ChatModel
import org.oremif.deepseek.models.ResponseFormat
import org.oremif.deepseek.models.chatCompletionParams

class AiRepository {
    private val deepseekApiKey = "sk-1825ec96e3364d37874ea10a91bb2c73"
    private val client = DeepSeekClient(deepseekApiKey) {
        chatCompletionTimeout(120_000)
    }
    private val systemPrompt = "The user will provide a weather forecast.\nYour job is to pick 3 different time intervals for each day to suggest activities for based on the weather at that time, the location and other requirements defined in the user prompt."
    private val examplesPrompt =
        """
        All output should be written in Norwegian
        Based on the weather forecast and the users location, pick 3 different time intervals for every single day to suggest activities for the next 7 days.
        In total there should be at most 21 different activities, spanning across the next 7 days and 3 time intervals per day.
        Activities should be realistic and available to do around the user's location.
        Activities has to match the weather forecast, during rainfall and/or low temperatures, more inside activities should be suggested, but not always, for example you can fish in the rain.
        Within the "activity" field should also be a brief explanation for where you could do the activity.
        Not all activities have to be physical.
        Activities suggested could be inside or outside activities.
        
        NOTE THAT EXAMPLE INPUTS AND OUTPUTS ARE SHORTENED VERSIONS OF THE ACTUAL INPUTS AND OUTPUT YOU WILL PRODUCE.
        
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
                    "month":"6",
                    "dayOfMonth":"24",
                    "timeStart":"14:00",
                    "timeEnd":"16:00",
                    "activity":"Svømming ved Badedammen Grorud"
                }
                {
                    "month":"6",
                    "dayOfMonth":"25",
                    "timeStart":"11:00",
                    "timeEnd":"13:00",
                    "activity":"Bowling på Veitvet senter"
                }
            ]
        }
        
        END OF EXAMPLES
        """.trimIndent()

    private val params = chatCompletionParams {
        model = ChatModel.DEEPSEEK_CHAT
        temperature = 0.5
        responseFormat = ResponseFormat.jsonObject

    }

    suspend fun getResponse(prompt: Prompt): JsonResponse? = withContext(Dispatchers.IO) {
        val response: ChatCompletion = client.chat(params) {
            system(systemPrompt)
            user("$examplesPrompt\n\nFollowing is the user prompt:\n\n<<<\n$prompt\n>>>")
        }
        val parsedResponse = response.choices[0].message.content?.let {
            Json.decodeFromString<JsonResponse>(
                it
            )
        }
        return@withContext parsedResponse
    }

}