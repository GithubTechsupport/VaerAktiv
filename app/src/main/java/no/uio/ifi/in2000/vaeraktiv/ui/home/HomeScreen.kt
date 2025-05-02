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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(isOnline: Boolean, viewModel: HomeScreenViewModel) {
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val activities by viewModel.activities.observeAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    LaunchedEffect(key1 = currentLocation) {
        currentLocation?.let {
            viewModel.getHomeScreenData()
        }
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else if (isOnline) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = BackGroundColor)
                ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Always show the current location.
                item {
                    CurrentLocation(uiState.locationName, currentLocation) // ikke riktig data for currentLocation (skal være lokasjon til device)
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
                        DisplayWeather(uiState.weatherToday, uiState)
                    }
                }

                // Today's Activities (always expanded)
                item {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .background(Container, shape = RoundedCornerShape(10.dp)),
                        horizontalAlignment = Alignment.Start
                    ){
                        Text(
                            text = "I dag",
                            style = MaterialTheme.typography.headlineSmall,
                            color = OnContainer,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                        )

                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ){
                            if (uiState.isErrorActivitiesToday) {
                                ErrorMessage(
                                    message = "Error fetching today's activities: ${uiState.errorMessageActivitiesToday}"
                                )
                            } else if (uiState.isLoadingActivitiesToday) {
                                LoadAllActivities()
                            } else if (activities?.get(0) != null) {
                                val todaysActivities = activities?.get(0)!!.activities
                                AddActivitiesForDay(
                                    dayNr = 0,
                                    activityDate = ActivityDate("I dag", todaysActivities),
                                    isLoading = {uiState.loadingActivities},
                                    onRefresh = { dayNr, indexParam -> viewModel.replaceActivityInDay(dayNr, indexParam ) },
                                    onViewInMap = {activity -> viewModel.viewActivityInMap(activity)}
                                )
                            }
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
                            activities = activities,
                            viewModel = viewModel,
                            uiState = uiState
                        )
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
