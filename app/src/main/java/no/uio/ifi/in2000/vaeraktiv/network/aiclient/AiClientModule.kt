package no.uio.ifi.in2000.vaeraktiv.network.aiclient

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
object AiClientModule {

    @Provides
    @Singleton
    @Named("OpenAi-Client")
    fun provideOpenAiClient(): AiClient {
        val apiKey = "YOUR_OPENAI_API_KEY"
        val azureHost = OpenAIHost.azure(
            resourceName = "YOUR_RESOURCE_NAME",
            deploymentId = "YOUR_DEPLOYMENT_ID",
            apiVersion = "YOUR_API_VERSION" // e.g., "2024-05-13"
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