package no.uio.ifi.in2000.vaeraktiv.network.httpclient

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton


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