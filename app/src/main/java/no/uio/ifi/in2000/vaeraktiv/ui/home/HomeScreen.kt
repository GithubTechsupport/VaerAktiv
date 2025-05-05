package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.ui.activity.PreferencesViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    isOnline: Boolean,
    viewModel: HomeScreenViewModel,
    preferencesViewModel: PreferencesViewModel,
    navController: NavHostController
) { // Manifest.xml. Ternger kanskje ikke resizable linjen
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val deviceLocation by viewModel.deviceLocation.observeAsState()
    val activities by viewModel.activities.observeAsState()
    Log.d("HomeScreen", "Devicelocation: $deviceLocation")
    LaunchedEffect(Unit) { viewModel.initialize() }
    LaunchedEffect(currentLocation) {
        currentLocation?.let { viewModel.getHomeScreenData() }
    }

    when {
        uiState.isLoading -> LoadingScreen()
        isOnline -> HomeContent(uiState, deviceLocation, activities, viewModel, preferencesViewModel, navController)
        else -> ErrorMessage("You're offline.")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeContent(
    uiState: HomeScreenUiState,
    deviceLocation: Location?,
    activities: List<SuggestedActivities?>?,
    viewModel: HomeScreenViewModel,
    preferencesViewModel: PreferencesViewModel,
    navController: NavHostController
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
            item { CurrentLocation(uiState.locationName, deviceLocation, setCurrentLocation = { location -> viewModel.setCurrentLocation(location)  }, navController) }

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



@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier.padding(8.dp)) {
    Text(
        text = message,
        color = Color.Red,
        modifier = modifier
    )
}
