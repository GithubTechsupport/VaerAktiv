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
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationDataSource
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
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

    @Singleton
    @Provides
    fun provideLocationForecastRepository(
        dataSource: LocationForecastDataSource
    ): LocationForecastRepository {
        return LocationForecastRepository(dataSource)
    }

    @Singleton
    @Provides
    fun provideFavoriteLocationDataSource(
        @ApplicationContext context: Context
    ): FavoriteLocationDataSource {
        return FavoriteLocationDataSource(context)
    }

    @Provides
    @Singleton
    fun provideFavoriteLocationRepository(
        dataSource: FavoriteLocationDataSource,
        geocoder: GeocoderClass
    ): FavoriteLocationRepository {
        return FavoriteLocationRepository(dataSource, geocoder)
    }

    @Provides
    @Singleton
    fun provideMetAlertsRepository(
        dataSource: MetAlertsDataSource,
    ): MetAlertsRepository {
        return MetAlertsRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideSunriseRepository(
        dataSource: SunriseDataSource,
    ): SunriseRepository {
        return SunriseRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(
        metAlertsRepository: MetAlertsRepository,
        locationForecastRepository: LocationForecastRepository,
        sunriseRepository: SunriseRepository,
        favoriteLocationRepo: FavoriteLocationRepository
    ): WeatherRepository {
        return WeatherRepository(metAlertsRepository, locationForecastRepository, sunriseRepository, favoriteLocationRepo)
    }


}