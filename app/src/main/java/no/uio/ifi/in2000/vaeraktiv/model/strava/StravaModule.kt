package no.uio.ifi.in2000.vaeraktiv.model.strava

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
fun interface StravaModule {

    @Binds
    @Singleton
    fun bindTokenStorage(
        dataStoreTokenStorage: DataStoreTokenStorage
    ): TokenStorage
}