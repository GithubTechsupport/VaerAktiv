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