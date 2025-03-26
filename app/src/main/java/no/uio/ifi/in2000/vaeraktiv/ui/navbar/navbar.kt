package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityScreen
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreen
import no.uio.ifi.in2000.vaeraktiv.ui.location.LocationScreen

@Composable
fun Navbar() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(navController, startDestination = "home", Modifier.padding(innerPadding)) {
            composable("home") { HomeScreen(navController) }
            composable("activity") { ActivityScreen() }
            composable("location") { LocationScreen() }
        }
    }
}

