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
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground


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
                item { CurrentLocation("Oslo") }                         // Lokasjon
                item { DisplayWarning("Masse skummelt kan skje i dag") } // Advarsel hvis det er noe å varsle om
                item { DisplayWeather("10", "2", "4mm", "5m/s") }        // Alle dataene vi trenger ish
                item { TodaysWeather(10, 18, "Skyet") }                  // Været i dag
                item { TodaysActivity(data) }                            // Dagens aktivitet
                item { WeatherWeek(dummyWeatherData) }                   // Været resten av uken
                item { SunRiseSet("08:20", "23:45") }                    // Sol opp/ned
            }
        }
    }
}


