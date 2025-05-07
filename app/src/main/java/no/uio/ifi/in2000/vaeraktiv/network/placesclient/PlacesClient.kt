package no.uio.ifi.in2000.vaeraktiv.network.placesclient

import android.content.Context
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlacesClientModule {
    @Provides
    @Singleton
    fun providePlacesClientModule(
        @ApplicationContext context: Context
    ): PlacesClient {
        Places.initializeWithNewPlacesApiEnabled(context, "AIzaSyAd7O7mvzNTGqRaFYhWWsJdJvq-GyVbedA")
        return Places.createClient(context)
    }
}