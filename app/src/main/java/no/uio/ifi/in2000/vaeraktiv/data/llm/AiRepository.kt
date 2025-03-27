package no.uio.ifi.in2000.vaeraktiv.data.llm

import org.oremif.deepseek.api.chat
import org.oremif.deepseek.client.DeepSeekClient
import org.oremif.deepseek.models.ChatCompletion
import org.oremif.deepseek.models.ChatModel
import org.oremif.deepseek.models.ResponseFormat
import org.oremif.deepseek.models.chatCompletionParams


class AiRepository {
    private val deepseekApiKey = System.getenv("DEEPSEEK_API_KEY")
    private val client = DeepSeekClient(deepseekApiKey)
    private val systemPrompt = "The user will provide a weather forecast. Your job is to pick 3 different time intervals to suggest activities for based on the weather at that time, for the next 5 days.\n\n"
    private val examplesPrompt =
        "BEGINNING OF EXAMPLES:\n" +
        "EXAMPLE INPUT:\n" +
        "EXAMPLE JSON OUTPUT:\n" +
        "EXAMPLE INPUT:\n" +
        "EXAMPLE JSON OUTPUT:\n" +
        "END OF EXAMPLES\n"
    private val completeSystemPrompt = systemPrompt + examplesPrompt

    private val params = chatCompletionParams {
        model = ChatModel.DEEPSEEK_CHAT
        temperature = 0.3
        responseFormat = ResponseFormat.jsonObject
    }

    suspend fun getResponse(prompt: String) {
        val response: ChatCompletion = client.chat(params) {
            system(systemPrompt)
            user(prompt)
        }
    }

    fun generateJsonExampleOutput() {

    }
}