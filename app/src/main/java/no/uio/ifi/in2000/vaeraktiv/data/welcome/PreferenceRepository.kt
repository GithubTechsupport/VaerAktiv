package no.uio.ifi.in2000.vaeraktiv.data.welcome

import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(private val preferenceDataSource: PreferenceDataSource) {

    val userPreference: StateFlow<List<Preference>>
        get() = preferenceDataSource.userPreference

    fun getEnabledPreferences(): List<Preference> {
        return preferenceDataSource.getEnabledPreferences()
    }

    suspend fun updatePreference(preferenceDesc: String, isEnabled: Boolean) {
        preferenceDataSource.updatePreference(preferenceDesc, isEnabled)
    }
}