package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.ui.DataSection
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    uiState: HomeScreenUiState,
    currentLocation: Location?,
    deviceLocation: (Location)->Unit,
    activities: List<SuggestedActivities?>?,
    viewModel: HomeScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { CurrentLocation(uiState.locationName, currentLocation, deviceLocation) }

            item {
                DataSection(
                    data = uiState.alerts,
                    error = uiState.alertsError,
                    errorMessagePrefix = "Error fetching alerts"
                ) { DisplayWarning(it) }
            }

            item {
                DataSection(
                    data = uiState.weatherToday,
                    error = uiState.weatherTodayError,
                    errorMessagePrefix = "Error fetching today's weather"
                ) { DisplayWeather(it, uiState) }
            }

            item {
                TodaysActivitiesSection(uiState, activities, viewModel)
            }

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