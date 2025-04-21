package no.uio.ifi.in2000.vaeraktiv.network.placesclient

import android.content.Context
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.aallam.openai.client.OpenAIHost
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.vaeraktiv.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesClientModule {
    @Provides
    @Singleton
    fun providePlacesClientModule(
        @ApplicationContext context: Context
    ): PlacesClient {
        Places.initializeWithNewPlacesApiEnabled(context, BuildConfig.PLACES_API_KEY)
        return Places.createClient(context)
    }
}