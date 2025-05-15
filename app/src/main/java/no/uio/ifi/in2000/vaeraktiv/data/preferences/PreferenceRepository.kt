package no.uio.ifi.in2000.vaeraktiv.data.preferences

import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides preference flow and formatted preference string for AI prompts.
 */
@Singleton
class PreferenceRepository @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource
) {

    val userPreference: StateFlow<List<Preference>>
        get() = preferenceDataSource.userPreference

    /**
     * Builds a prompt string listing enabled preferences.
     */
    fun getEnabledPreferences(): String {
        val preferences = preferenceDataSource.getEnabledPreferences()
        return if (preferences.isNotEmpty()) {
            """
            Following are the user's preferences.
            Activities should try to match these preferences:
            
            ${preferences.joinToString(separator = "\n") { preference ->
            preference.desc
            }}
            
            Note that if "Aktiviteter som koster penger" is enabled, the user is open to activities that cost money, but they don't have to cost money.
            If "Aktiviteter som koster penger" is not in the user's preferences, then users should NOT get activities that cost money.
            
            
            """.trimIndent()
        } else {
            ""
        }
    }

    /**
     * Toggles and persists a preference by description.
     */
    suspend fun updatePreference(preferenceDesc: String, isEnabled: Boolean) {
        preferenceDataSource.updatePreference(preferenceDesc, isEnabled)
    }
}
