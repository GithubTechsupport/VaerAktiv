package no.uio.ifi.in2000.vaeraktiv.network.httpclient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class NetworkClient @Inject constructor(private val json: Json) {
    val ktorHttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    @Named("prettyPrint-ignoreUnknownKeys")
    fun providePrettyIgnoreJson(): Json {
        return Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }
    }
    @Provides
    @Singleton
    @Named("prettyPrint-ignoreUnknownKeys-Client")
    fun providePrettyIgnoreClient(@Named("prettyPrint-ignoreUnknownKeys") json: Json): NetworkClient {
        return NetworkClient(json)
    }

    @Provides
    @Singleton
    @Named("prettyPrint-isLenient-ignoreUnknownKeys")
    fun providePrettyLenientJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
        }
    }
    @Provides
    @Singleton
    @Named("prettyPrint-isLenient-ignoreUnknownKeys-Client")
    fun providePrettyLenientClient(@Named("prettyPrint-isLenient-ignoreUnknownKeys") json: Json): NetworkClient {
        return NetworkClient(json)
    }

    @Provides
    @Singleton
    @Named("ignoreUnknownKeys")
    fun provideIgnoreUnknownJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }
    @Provides
    @Singleton
    @Named("ignoreUnknownKeys-Client")
    fun provideIgnoreUnknownClient(@Named("ignoreUnknownKeys") json: Json): NetworkClient {
        return NetworkClient(json)
    }
}