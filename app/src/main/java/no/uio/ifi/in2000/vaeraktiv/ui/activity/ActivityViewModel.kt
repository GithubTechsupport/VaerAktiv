package no.uio.ifi.in2000.vaeraktiv.ui.activity

import android.util.Log
import androidx.lifecycle.LifecycleOwner
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
class ActivityViewModel @Inject constructor(private val weatherRepository: WeatherRepository) : ViewModel() {

    private var initialized = false

    val currentLocation: LiveData<Location?> = weatherRepository.currentLocation

    private val _activityScreenUiState = MutableStateFlow(
        ActivityScreenUiState()
    )

    val activityScreenUiState: StateFlow<ActivityScreenUiState> = _activityScreenUiState.asStateFlow()

    fun startTracking(lifecycleOwner: LifecycleOwner) {
        weatherRepository.trackDeviceLocation(lifecycleOwner)
    }

    fun initialize() {
        if (initialized) return
        initialized = true
        getActivities()
    }

    fun getActivities() {
        viewModelScope.launch{
            _activityScreenUiState.value = ActivityScreenUiState(isLoading = true)
            try {
                Log.d("ActivityViewModel", "Calling getActivities")
                Log.d("ActivityViewModel", "Current location is: " + currentLocation.value)
                val activities = weatherRepository.getActivities(Location("Oslo Sentralstasjon, Oslo", 59.9111, 10.7533))
                _activityScreenUiState.update {
                    it.copy(
                        activities = activities
                    )
                }
            } catch (e: Exception) {
                _activityScreenUiState.update {
                    it.copy(
                        isError = true,
                        errorMessage = e.message ?: "Unknown error"
                    )

                }
                Log.d("ActivityViewModel", "Error is: $e")
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