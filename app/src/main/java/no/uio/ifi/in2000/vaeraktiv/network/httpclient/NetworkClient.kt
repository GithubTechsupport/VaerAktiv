package no.uio.ifi.in2000.vaeraktiv.network.httpclient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class NetworkClient @Inject constructor(private val json: Json) {
    private val userAgent = "VaerAktiv/1.0 (https://github.uio.no/IN2000-V25/team-31)"

    val ktorHttpClient = HttpClient(CIO) {
        install(HttpCache)
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            headers {
                append(HttpHeaders.UserAgent, userAgent)
            }
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
            ignoreUnknownKeys = true
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