package no.uio.ifi.in2000.vaeraktiv.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepositoryDefault
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.IMetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository

//@Module
//@InstallIn(SingletonComponent::class)
//abstract class RepositoryModule {
//
////    @Binds
////    abstract fun bindWeatherRepository(
////        impl: WeatherRepositoryDefault
////    ): WeatherRepository
//
////    @Binds
////    abstract fun bindMetAlertsRepository(
////        impl: MetAlertsRepository
////    ): IMetAlertsRepository
//}