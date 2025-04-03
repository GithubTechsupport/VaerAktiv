package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
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
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SolarEvent
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunEvent
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Properties

val dummyWarningData = listOf (
    Properties(
        riskMatrixColor = "yellow",
        eventAwarenessName = "Hei og hå",
        awareness_type = "3; snow",
        description = "Ikke gå ut"
    ),
    Properties(
        riskMatrixColor = "red",
        eventAwarenessName = "Hei og hå",
        awareness_type = "3; rain",
        description = "ta med paraply"
    ),
    Properties(
        riskMatrixColor = "red",
        eventAwarenessName = "Hei og hå",
        awareness_type = "3; Snow",
        description = null
    )
)

val dummyTodaysWeatherData = no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday(
    tempMax = "20",
    tempMin = "15",
    tempNow = "18",
    icon = "fog",
    iconNow = "rain",
    windSpeed = "5",
    precipitationAmount = "0",
    uv = "2"
)

val dummSunData = no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunProperties(
    body = "Noe",
    sunrise = SunEvent("12", 12.5),
    sunset = SunEvent("12", 12.5),
    solarnoon = SolarEvent(time = "12", discCentreElevation = 12.5, visible = true),
    solarmidnight = SolarEvent(time = "12", discCentreElevation = 12.5, visible = true)
)

val dummyAiResponse = "Jeg anbefaler kano ellerno sånt no kanskje kanskje maybe!"

val dummyLocation = "Bodø"
@Composable
fun HomeScreen(isOnline: Boolean, viewModel: HomeScreenViewModel) {
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

                // henter viewmodel senere
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.todaysWeather != null) {
                        item { CurrentLocation(uiState.locationName) } // Lokasjon
                        item { DisplayWarning(dummyWarningData) } // Advarsel hvis det er noe å varsle om
                        item { DisplayWeather(uiState.todaysWeather) } // Alle dataene vi trenger ish
                        item { TodaysWeather(uiState.todaysWeather) } // Været i dag
                        item { TodaysActivity(dummyAiResponse) } // Dagens aktivitet
                        item { WeatherWeek(uiState.thisWeeksWeather) } // Været resten av uken
                        item { SunRiseSet(dummSunData) } // Sol opp/ned
                    }
                }
            }
        }
    }
}


