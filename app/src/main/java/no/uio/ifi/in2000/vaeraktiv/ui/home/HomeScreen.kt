package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen

const val dummyAiResponse = "I dag er en perfekt dag for å sette seg på Los Tacos, ta seg en pils, og nyte sola!"

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
            viewModel.getHomeScreenData()
        }
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else if (uiState.isError) {
        Text(text = uiState.errorMessage)
    } else {
        if (isOnline) {

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
                    if (uiState.todaysWeather != null) {
                        item { CurrentLocation(uiState.locationName) } // Lokasjon
                        item { DisplayWarning(uiState.alerts) } // Advarsel hvis det er noe å varsle om
                        item { DisplayWeather(uiState.todaysWeather) } // Alle dataene vi trenger ish
                        item { TodaysWeather(uiState.todaysWeather) } // Været i dag
                        item { TodaysActivity(dummyAiResponse) } // Dagens aktivitet
                        item { WeatherWeek(uiState.thisWeeksWeather) } // Været resten av uken
                        item { SunRiseSet(uiState.sunRiseSet) } // Sol opp/ned
                    }
                }
            }
        }
    }
}


