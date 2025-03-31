package no.uio.ifi.in2000.vaeraktiv.data.ai

import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
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
    private val systemPrompt = "The user will provide a weather forecast. Your job is to pick 3 different time intervals for each day to suggest activities for based on the weather at that time, for the next 5 days."
    private val examplesPrompt =
        """
        NOTE THAT EXAMPLE INPUTS AND OUTPUTS ARE SHORTENED VERSIONS OF THE ACTUAL INPUTS AND OUTPUT YOU WILL PRODUCE.
        TEMPERATURE IS IN CELSIUS.
        
        BEGINNING OF EXAMPLES
        
        EXAMPLE INPUT:
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
        
        EXAMPLE JSON OUTPUT:
        [
            {
                "month":"6",
                "dayOfMonth":"24",
                "timeStart":"14:00",
                "timeEnd":"16:00",
                "activity":"Go swimming"
            }
            {
                "month":"6",
                "dayOfMonth":"25",
                "timeStart":"11:00",
                "timeEnd":"13:00",
                "activity":"Go bowling"
            }
        ]
        
        END OF EXAMPLES
        """.trimIndent()

    private val params = chatCompletionParams {
        model = ChatModel.DEEPSEEK_CHAT
        temperature = 0.3
        responseFormat = ResponseFormat.jsonObject

    }

    suspend fun getResponse(prompt: String): ChatCompletion {
        val response: ChatCompletion = client.chat(params) {
            system(systemPrompt)
            user("$examplesPrompt\n\nFollowing is the user prompt:\n$prompt")
        }
        return response
    }

}

suspend fun main() {
    val aiRepository = AiRepository()
    val locationForecastDataSource = LocationForecastDataSource()
    val response = locationForecastDataSource.getResponse("https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=60&lon=11")
    val prompt = Prompt(response.properties, 5)
    //println(prompt)
    println(aiRepository.getResponse(prompt.toString()).choices[0].message.content)
}