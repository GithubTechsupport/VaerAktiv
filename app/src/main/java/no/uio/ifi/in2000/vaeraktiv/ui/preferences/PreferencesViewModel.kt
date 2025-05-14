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

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {

    val userPreferences: StateFlow<List<Preference>> = preferenceRepository.userPreference

    private val _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    fun onPreferenceToggled(preference: Preference, isEnabled: Boolean) {
        viewModelScope.launch {
            preferenceRepository.updatePreference(preference.desc, isEnabled)
        }
    }

    fun navigateBack() {
        _navigateBack.value = true
    }

    fun onNavigationHandled() {
        _navigateBack.value = false
    }
}