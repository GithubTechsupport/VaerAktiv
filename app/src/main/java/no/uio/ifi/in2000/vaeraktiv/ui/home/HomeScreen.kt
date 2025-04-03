package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen


@Composable
fun HomeScreen(navController: NavController, isOnline: Boolean, viewModel: HomeScreenViewModel) {
    val data = emptyList<String>()
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val dummyWeatherData = listOf(
        ForecastForDay(date = "26. mars", maxTemp = "8", icon = "clearsky_day"),
        ForecastForDay(date = "27. mars", maxTemp = "1", icon = "cloudy"),
        ForecastForDay(date = "28. mars", maxTemp = "7", icon = "fog"),
        ForecastForDay(date = "29. mars", maxTemp = "6", icon = "fog"),
        ForecastForDay(date = "30. mars", maxTemp = "9", icon = "snow"),
        ForecastForDay(date = "31. mars", maxTemp = "1", icon = "snow"),
        ForecastForDay(date = "1. april", maxTemp = "1", icon = "fog"),
        ForecastForDay(date = "2. april", maxTemp = "1", icon = "snow")
    )

    val currentLocation by viewModel.currentLocation.observeAsState()
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
    } else if (uiState.isError) {
        Text(text = uiState.errorMessage)
    } else {
        if(isOnline) {

            Column (modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF6BAEDF)),
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                // henter viewmodel senere
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item { CurrentLocation(uiState.locationName) } // Lokasjon
                    item { DisplayWarning("Masse skummelt kan skje i dag") } // Advarsel hvis det er noe å varsle om
                    if (uiState.todaysWeather != null) {
                        item { DisplayWeather(uiState.todaysWeather!!.tempNow, uiState.todaysWeather!!.uv, uiState.todaysWeather!!.precipitationAmount, uiState.todaysWeather!!.windSpeed) } // Alle dataene vi trenger ish
                        item { TodaysWeather(uiState.todaysWeather!!.tempMin, uiState.todaysWeather!!.tempMax, uiState.todaysWeather!!.iconNow) } // Været i dag
                        item { TodaysActivity(data) } // Dagens aktivitet
                        item { WeatherWeek(uiState.thisWeeksWeather) } // Været resten av uken
                        item { SunRiseSet("08:20", "23:45") } // Sol opp/ned
                    }

                }
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen(navController = NavController(LocalContext.current))
}


