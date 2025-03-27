package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.time.delay
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkObserver
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityScreen
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreen
import no.uio.ifi.in2000.vaeraktiv.ui.location.LocationScreen
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navbar() {
    val navController = rememberNavController()
    var isOnline by remember { mutableStateOf(true) }
    var showNoNetworkDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    NetworkObserver { newStatus ->
        isOnline = newStatus
        showNoNetworkDialog = !newStatus
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                LoadingScreen() // Show LoadingScreen instead of NavHost
                LaunchedEffect(Unit) {
                    delay(Duration.ofSeconds(1))
                    isLoading = false
                    if (!isOnline) {
                        showNoNetworkDialog = true
                    }
                }
            } else {
                // Show NavHost only when not loading.
                NavHost(navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController, isOnline) }
                    composable("activity") { ActivityScreen(isOnline) }
                    composable("location") { LocationScreen(isOnline) }
                }
                if (showNoNetworkDialog) {
                    NoNetworkDialog(
                        onRetry = {
                            isLoading = true
                            showNoNetworkDialog = false
                        },
                        onClose = {
                            (context as? MainActivity)?.finishAffinity()
                        }
                    )
                }
            }
        }
    }
}