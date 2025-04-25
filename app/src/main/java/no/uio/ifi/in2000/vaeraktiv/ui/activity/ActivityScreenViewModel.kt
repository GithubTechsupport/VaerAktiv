package no.uio.ifi.in2000.vaeraktiv.ui.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
import javax.inject.Inject

@HiltViewModel
class ActivityScreenViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = weatherRepository.currentLocation

    private val _activityScreenUiState = MutableStateFlow(
        ActivityScreenUiState()
    )

    val activityScreenUiState: StateFlow<ActivityScreenUiState> = _activityScreenUiState.asStateFlow()

    fun initialize() {
        if (initialized) return
        initialized = true
        getActivities()
    }

    fun getActivities() {
        viewModelScope.launch {
            _activityScreenUiState.update {
                it.copy(isLoading = true)
            }
            try {
                if (currentLocation.value != null) {
                    val placesSuggestions = weatherRepository.getNearbyPlaces(currentLocation.value!!)
                    Log.d("ActivityViewModel", placesSuggestions.toString())
                    val activities = weatherRepository.getActivities(currentLocation.value!!)
                    _activityScreenUiState.update {
                        it.copy(
                            activities = activities
                        )
                    }
                }
            } catch (e: Exception) {
                _activityScreenUiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.toString() ?: "Unknown error"
                    )

                }
                Log.e("ActivityViewModel", "Error: ", e)
            } finally {
                _activityScreenUiState.update {
                    it.copy(
                        isLoading = false
                    )
                }
            }
        }
    }

    // BARE FOR TESTING. Kan tas vekk senere.
    /*
    fun getAlertForLocation() {
    viewModelScope.launch {
        val alert = weatherRepository.getAlertForLocation(Location("Tromsø", 69.64, 18.95))
        Log.d("ActivityViewModel", "Alert for location: ${alert[0].description}")
        }
    }
 */



}

data class ActivityScreenUiState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val activities: JsonResponse? = null
)