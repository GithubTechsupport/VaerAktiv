package no.uio.ifi.in2000.vaeraktiv.ui.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.preferences.PreferenceRepository
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference
import javax.inject.Inject

/**
 * ViewModel for user preference toggles and navigation back event.
 */
@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    /** Stream of enabled preferences from repository. */
    val userPreferences: StateFlow<List<Preference>> = preferenceRepository.userPreference

    private val _navigateBack = MutableLiveData(false)
    /** LiveData signaling navigation back to previous screen. */
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    /**
     * Toggles a preference on/off in repository.
     */
    fun onPreferenceToggled(preference: Preference, isEnabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.updatePreference(preference.desc, isEnabled)
        }
    }

    /** Triggers navigation back flag. */
    fun navigateBack() {
        _navigateBack.value = true
    }

    /** Resets navigation back flag after handled. */
    fun onNavigationHandled() {
        _navigateBack.value = false
    }
}
