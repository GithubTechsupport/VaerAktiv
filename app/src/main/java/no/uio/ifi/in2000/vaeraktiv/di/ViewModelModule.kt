package no.uio.ifi.in2000.vaeraktiv.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideHomeScreenViewModel(
        weatherRepository: WeatherRepository,
        deviceDateTimeRepository: DeviceDateTimeRepository
    ): HomeScreenViewModel {
        return HomeScreenViewModel(weatherRepository, deviceDateTimeRepository)
    }
}