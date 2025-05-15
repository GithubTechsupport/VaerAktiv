package no.uio.ifi.in2000.vaeraktiv.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideHomeScreenViewModel(
        aggregateRepository: IAggregateRepository,
        deviceDateTimeRepository: DeviceDateTimeRepository
    ): HomeScreenViewModel {
        return HomeScreenViewModel(aggregateRepository, deviceDateTimeRepository)
    }
}