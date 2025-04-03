package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

val dummyTodaysWeatherData = no.uio.ifi.in2000.vaeraktiv.model.ui.TodaysWeatherData(
    tempMax = "20",
    tempMin = "15",
    tempNow = "18",
    iconDesc = "fog",
    iconDescNow = "rain",
    wind = "5",
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
fun HomeScreen (navController: NavController, isOnline: Boolean) {
    val data = emptyList<String>()

    if(isOnline){
        Column (modifier = Modifier
            .fillMaxSize()
            .background(
            Brush.verticalGradient(
                colors = listOf(MainBackground, SecondaryBackground)
            )
        ),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            // henter viewmodel senere
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { CurrentLocation(dummyLocation) } // Lokasjon
                item { DisplayWarning(dummyWarningData) } // Advarsel hvis det er noe å varsle om
                item { DisplayWeather(dummyTodaysWeatherData) } // Alle dataene vi trenger ish
                item { TodaysWeather(dummyTodaysWeatherData) } // Været i dag
                item { TodaysActivity(dummyAiResponse) } // Dagens aktivitet
                item { WeatherWeek(dummyWeatherData) } // Været resten av uken
                item { SunRiseSet(dummSunData) } // Sol opp/ned
            }
        }
    }
}


