package no.uio.ifi.in2000.vaeraktiv.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import no.uio.ifi.in2000.vaeraktiv.model.ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import javax.inject.Inject
import javax.inject.Named

class AiRepository @Inject constructor(@Named("OpenAi-Client") private val client: AiClient) {
    suspend fun getResponse(prompt: Prompt): JsonResponse? = withContext(Dispatchers.IO) {
        return@withContext try {
            client.getResponse(prompt)
        } catch (e: Exception) {
            throw e
        }
    }
}