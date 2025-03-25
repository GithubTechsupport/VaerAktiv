package no.uio.ifi.in2000.vaeraktiv.di

import android.app.Application
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGeocodeClass(
        @ApplicationContext context: Context
    ): GeocoderClass {
        return GeocoderClass(context)
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun provideLocationDataSource(
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationDataSource {
        return LocationDataSource(context, fusedLocationProviderClient)
    }
}