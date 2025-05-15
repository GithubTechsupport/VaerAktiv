package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.ui.DataSection

/**
 * Main content composable for the Home screen.
 *
 * Displays the user's current location, weather alerts, today's weather,
 * suggested activities, and the weekly weather forecast.
 *
 * @param uiState The current UI state containing weather, alerts, and location data.
 * @param deviceLocation The device's current geographic location, if available.
 * @param activities List of suggested activities.
 * @param viewModel The ViewModel responsible for handling UI logic and data updates.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    uiState: HomeScreenUiState,
    deviceLocation: Location?,
    activities: List<SuggestedActivities?>?,
    viewModel: HomeScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display the current location with option to change or navigate to preferences
            item {
                CurrentLocation(
                    uiState.locationName,
                    deviceLocation,
                    setCurrentLocation = { location -> viewModel.setCurrentLocation(location) },
                    navigateToPreferences = { viewModel.navigateToPreferences() }
                )
            }

            // Display weather alerts section, handle loading and errors
            item {
                DataSection(
                    data = uiState.alerts,
                    error = uiState.alertsError,
                    errorMessagePrefix = "Error fetching alerts"
                ) { DisplayWarning(it) }
            }

            // Display today's weather section, handle loading and errors
            item {
                DataSection(
                    data = uiState.weatherToday,
                    error = uiState.weatherTodayError,
                    errorMessagePrefix = "Error fetching today's weather"
                ) { DisplayWeather(it, uiState) }
            }

            // Display today's suggested activities section
            item {
                TodaysActivitiesSection(uiState, activities, viewModel)
            }

            // Display the weekly weather forecast section, handle errors
            item {
                DataSection(
                    data = Unit,
                    error = uiState.thisWeeksWeatherError,
                    errorMessagePrefix = "Error fetching weekly forecast"
                ) {
                    WeatherWeek(activities, viewModel, uiState)
                }
            }
        }
    }
}