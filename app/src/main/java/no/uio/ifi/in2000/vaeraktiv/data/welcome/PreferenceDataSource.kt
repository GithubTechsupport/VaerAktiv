package no.uio.ifi.in2000.vaeraktiv.data.welcome

import com.google.gson.Gson
import android.content.Context
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference
import java.io.File
import java.io.IOException
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(@ApplicationContext private val context: Context) {

    private val gson = Gson()
    private val fileName = "user_preferences.json"
    private val file: File = File(context.filesDir, fileName)

    private val _userPreference = MutableStateFlow<List<Preference>>(emptyList())
    val userPreference = _userPreference

    private val allPossiblePreferences = listOf(
        Preference("Ballsport", false),
        Preference("Håndtverk", false),
        Preference("Friluftsliv", false),
        Preference("Gå turer", false),
        Preference("Trening", false),
        Preference("Sammen med andre", false),
        Preference("Alene", false),
        Preference("Aktiviteter som koster penger", false)
    )

    init {
        if (!file.exists()) {
            file.writeText("[]")
        }

        loadPreferences()
    }

    private fun loadPreferences() {
        if (!file.exists()) {
            _userPreference.value = allPossiblePreferences
            savePreference(_userPreference.value)
            return
        }

        try {
            val jsonString = file.readText()
            if (jsonString.isEmpty()) {
                _userPreference.value = allPossiblePreferences
                savePreference(_userPreference.value)
                return
            }

            val type = object : TypeToken<List<Preference>>() {}.type
            val loadedPreferences: List<Preference>? = gson.fromJson(jsonString, type)

            if (loadedPreferences.isNullOrEmpty()) {
                _userPreference.value = allPossiblePreferences
                savePreference(_userPreference.value)
                return
            }

            // Merge loaded preferences with all possible preferences
            val updatedPreferences = allPossiblePreferences.map { possiblePref ->
                loadedPreferences.find { it.desc == possiblePref.desc }?.let { loadedPref ->
                    possiblePref.copy(isEnabled = loadedPref.isEnabled)
                } ?: possiblePref.copy(isEnabled = false)
            }
            _userPreference.value = updatedPreferences

        } catch (e: Exception) { // Catch all exceptions, including JsonSyntaxException
            e.printStackTrace()
            _userPreference.value = allPossiblePreferences
            savePreference(_userPreference.value)
        }
    }

    // Saves the current list of preferences (with updated isEnabled states) to the file
    private fun savePreference(preferences: List<Preference>) {
        try {
            val jsonString = gson.toJson(preferences)
            file.writeText(jsonString)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    suspend fun updatePreference(preferenceDesc: String, isEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            val currentPreferences = _userPreference.value.toMutableList()
            val index = currentPreferences.indexOfFirst { it.desc == preferenceDesc }
            if (index != -1) {
                currentPreferences[index] = currentPreferences[index].copy(isEnabled = isEnabled)
                _userPreference.value = currentPreferences.toList() // Update StateFlow
                savePreference(currentPreferences) // Save changes to the file
            }
        }
    }

    fun getEnabledPreferences(): List<Preference> {
        return _userPreference.value.filter { it.isEnabled }
    }
}