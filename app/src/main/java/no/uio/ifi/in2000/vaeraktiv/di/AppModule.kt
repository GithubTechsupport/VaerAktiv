package no.uio.ifi.in2000.vaeraktiv.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeDataSource
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepositoryDefault
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationDataSource
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationDataSource
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.placesRepository
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepositoryDefault
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.data.welcome.PreferenceRepository
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

    @Singleton
    @Provides
    fun provideLocationRepository(
        dataSource: LocationDataSource
    ): LocationRepository {
        return LocationRepository(dataSource)
    }

    @Singleton
    @Provides
    fun provideNowcastRepository(
        dataSource: NowcastDataSource
    ): NowcastRepository {
        return NowcastRepository(dataSource)
    }

    @Singleton
    @Provides
    fun provideDeviceDateTimeRepository(
        dataSource: DeviceDateTimeDataSource
    ): DeviceDateTimeRepository {
        return DeviceDateTimeRepositoryDefault(dataSource)
    }

    @Singleton
    @Provides
    fun provideDeviceDateTimeDataSource(): DeviceDateTimeDataSource {
        return DeviceDateTimeDataSource()
    }

    @Singleton
    @Provides
    fun providePlacesRepository(placesClient: PlacesClient): placesRepository {
        return placesRepository(placesClient)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        metAlertsRepository: MetAlertsRepository,
        locationForecastRepository: LocationForecastRepository,
        sunriseRepository: SunriseRepository,
        favoriteLocationRepo: FavoriteLocationRepository,
        aiRepository: AiRepository,
        geocoder: GeocoderClass,
        locationRepository: LocationRepository,
        nowcastRepository: NowcastRepository,
        placesRepository: placesRepository,
        stravaRepository: StravaRepository,
        preferenceRepository: PreferenceRepository
    ): WeatherRepository {
        return WeatherRepositoryDefault(
            metAlertsRepository, locationForecastRepository, sunriseRepository,
            favoriteLocationRepo, aiRepository, locationRepository, geocoder,
            nowcastRepository, placesRepository, stravaRepository, preferenceRepository
        )
    }


}