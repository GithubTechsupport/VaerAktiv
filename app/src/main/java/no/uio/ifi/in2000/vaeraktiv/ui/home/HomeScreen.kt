package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground
import java.time.LocalDate
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(isOnline: Boolean, viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let {
            Log.d("HomeScreen", "Current location changed: $it")
            viewModel.getHomeScreenData()
            viewModel.getActivities()
        }
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else if (isOnline) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MainBackground, SecondaryBackground)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Always show the current location.
                item {
                    CurrentLocation(uiState.locationName)
                }

                // Alerts Section
                item {
                    if (uiState.alertsError != null) {
                        ErrorMessage(
                            message = "Error fetching alerts: ${uiState.alertsError}"
                        )
                    } else {
                        DisplayWarning(uiState.alerts)
                    }
                }

                // Today's Weather Section
                item {
                    if (uiState.todaysWeatherError != null) {
                        ErrorMessage(
                            message = "Error fetching today's weather: ${uiState.todaysWeatherError}"
                        )
                    } else if (uiState.todaysWeather != null) {
                        DisplayWeather(uiState.todaysWeather)
                    }
                }
                item {
                    if (uiState.todaysWeatherError == null && uiState.todaysWeather != null) {
                        TodaysWeather(uiState.todaysWeather)
                    }
                }

                // Today's Activities
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dagens aktiviteter",
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                            RefreshButton (
                                onClick = {viewModel.getActivities()},
                                isLoading = uiState.isLoadingTodaysActivites
                            )
                        }
                        if (uiState.isErrorTodaysActivites) {
                            ErrorMessage(
                                message = "Error fetching today's activities: ${uiState.errorMessageTodaysActivites}"
                            )
                        } else if (uiState.todaysActivites != null) {
                            val activities = uiState.todaysActivites!!.activities.map {
                                Activity(
                                    "${it.timeStart} - ${it.timeEnd}",
                                    it.activity,
                                    it.activityDesc
                                )
                            }
                            AddActivitiesForDay(ActivityDate("I dag", activities))
                        } else {
                            Text (
                                text = "Finner ingen aktiviteter",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                items(6) { index ->
                    val date = LocalDate.now().plusDays(index + 1L)
                    val dateString = "${date.dayOfMonth}. ${Month.of(date.monthValue).name.lowercase()}"
                    val activities = uiState.futureActivities[date]
                    val isLoading = uiState.loadingFutureActivities.contains(date)
                    ActivityCard(
                        date = date,
                        dateString = dateString,
                        activities = activities,
                        isLoading = isLoading,
                        isError = uiState.isErrorFutureActivities,
                        errorMessage = uiState.errorMessageFutureActivities,
                        onExpand = { viewModel.getActivitesForDate(date) }
                    )
                }

                // Weekly Weather Forecast Section
                item {
                    if (uiState.thisWeeksWeatherError != null) {
                        ErrorMessage(
                            message = "Error fetching weekly forecast: ${uiState.thisWeeksWeatherError}"
                        )
                    } else {
                        WeatherWeek(uiState.thisWeeksWeather)
                    }
                }

                // SunRise/SunSet Section
                item {
                    if (uiState.sunRiseSetError != null) {
                        ErrorMessage(
                            message = "Error fetching sunrise/sunset data: ${uiState.sunRiseSetError}"
                        )
                    } else {
                        SunRiseSet(uiState.sunRiseSet)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier.padding(8.dp)) {
    Text(
        text = message,
        color = Color.Red,
        modifier = modifier
    )
}
