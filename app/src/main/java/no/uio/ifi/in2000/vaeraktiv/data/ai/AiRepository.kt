package no.uio.ifi.in2000.vaeraktiv.data.ai

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIHost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import org.oremif.deepseek.api.chat
import org.oremif.deepseek.client.DeepSeekClient
import org.oremif.deepseek.models.ChatCompletion
import org.oremif.deepseek.models.ChatModel
import org.oremif.deepseek.models.ResponseFormat
import org.oremif.deepseek.models.chatCompletionParams
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Duration.Companion.seconds

class AiRepository @Inject constructor(@Named("Deepseek-Client") private val client: AiClient) {

    suspend fun getResponse(prompt: Prompt): JsonResponse? = withContext(Dispatchers.IO) {
        return@withContext try {
            client.getResponse(prompt)
        } catch (e: Exception) {
            throw e
        }
    }
}