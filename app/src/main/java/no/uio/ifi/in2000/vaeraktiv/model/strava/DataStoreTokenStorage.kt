package no.uio.ifi.in2000.vaeraktiv.model.strava

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

private val Context.stravaDataStore by preferencesDataStore(name = "strava_auth")

@Singleton
class DataStoreTokenStorage @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenStorage {
    private object Keys {
        val ACCESS = stringPreferencesKey("access_token")
        val REFRESH = stringPreferencesKey("refresh_token")
        val EXPIRES = longPreferencesKey("expires_at")
    }

    override suspend fun getTokens(): StravaAuthTokens? {
        val prefs = context.stravaDataStore.data.first()
        val access = prefs[Keys.ACCESS] ?: return null
        val refresh = prefs[Keys.REFRESH] ?: return null
        val expires = prefs[Keys.EXPIRES] ?: return null
        return StravaAuthTokens(access, refresh, expires)
    }

    override suspend fun saveTokens(tokens: StravaAuthTokens) {
        context.stravaDataStore.edit { prefs ->
            prefs[Keys.ACCESS] = tokens.accessToken
            prefs[Keys.REFRESH] = tokens.refreshToken
            prefs[Keys.EXPIRES] = tokens.expiresAt
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
fun interface StravaModule {

    @Binds
    @Singleton
    fun bindTokenStorage(
        dataStoreTokenStorage: DataStoreTokenStorage
    ): TokenStorage
}