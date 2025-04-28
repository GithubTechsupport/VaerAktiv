package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground

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
                .background(color = MaterialTheme.colorScheme.background)
                ,
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
                    if (uiState.weatherTodayError != null) {
                        ErrorMessage(
                            message = "Error fetching today's weather: ${uiState.weatherTodayError}"
                        )
                    } else if (uiState.weatherToday != null) {
                        DisplayWeather(uiState.weatherToday)
                    }
                }
                item {
                    if (uiState.next24HoursError != null) {
                        ErrorMessage(
                            message = "Error fetching today's weather: ${uiState.next24HoursError}"
                        )
                    } else if (uiState.next24Hours != null) {
                        DisplayHourlyForecast(uiState.next24Hours)
                    }
                }
                item {
                    if (uiState.weatherTodayError == null && uiState.weatherToday != null) {
                        TodaysWeather(uiState.weatherToday)
                    }
                }

                // Today's Activities (always expanded)
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                            //.padding(horizontal = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ){
                        Text(
                            text = "Dagens anbefalte aktiviteter",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                        if (uiState.isErrorActivitiesToday) {
                            ErrorMessage(
                                message = "Error fetching today's activities: ${uiState.errorMessageActivitiesToday}"
                            )
                        } else if (uiState.activitiesToday != null) {
                            val activities = uiState.activitiesToday!!.activities.mapIndexed { index, response ->
                                Activity(
                                    timeOfDay = "${response.timeStart} - ${response.timeEnd}",
                                    name = response.activity,
                                    desc = response.activityDesc
                                ) to index
                            }
                            AddActivitiesForDay(
                                activityDate = ActivityDate("I dag", activities.map { it.first }),
                                onRefresh = { index -> viewModel.refreshSingleActivity(index) },
                                isRefreshing = { index -> uiState.isRefreshingActivity.contains(index) }
                            )
                        } else {
                            Text (
                                text = "Finner ingen aktiviteter",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                // Weekly Weather Forecast Section
                item {
                    if (uiState.thisWeeksWeatherError != null) {
                        ErrorMessage(
                            message = "Error fetching weekly forecast: ${uiState.thisWeeksWeatherError}"
                        )
                    } else {
                        WeatherWeek(
                            data = uiState.thisWeeksWeather,
                            viewModel = viewModel,
                            uiState = uiState
                        )
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
