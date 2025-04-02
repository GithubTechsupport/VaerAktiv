package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkConnection


@Composable
fun HomeScreen(navController: NavController, isOnline: Boolean, viewModel: HomeScreenViewModel) {
    val data = emptyList<String>()

    if(isOnline){
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
                item { CurrentLocation("Oslo") } // Lokasjon
                item { DisplayWarning("Masse skummelt kan skje i dag") } // Advarsel hvis det er noe å varsle om
                item { DisplayWeather("10", "2", "4mm", "5m/s") } // Alle dataene vi trenger ish
                item { TodaysWeather(10, 18, "Skyet") } // Været i dag
                item { TodaysActivity(data) } // Dagens aktivitet
                item { WeatherWeek(dummyWeatherData) } // Været resten av uken
                item { SunRiseSet("08:20", "23:45") } // Sol opp/ned
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    //HomeScreen(navController = NavController(LocalContext.current))
}


