package no.uio.ifi.in2000.vaeraktiv.ui.navbar

//noinspection UsingMaterialAndMaterial3Libraries
import android.os.Build
import android.util.Log
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
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkObserver
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityScreen
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreen
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.LocationScreen


/*
*
* This function is the navbar. This function is called in MainActivity.kt. When this function is called a display in the button wil be called, this display will
*  have tre buttons called from the ButtonNavigationBar function.
*The function also observe and update if the network status changes on the device. If the internet connection is lost a dialog will be displayed. if not the navbar is shown
* */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navbar (favoriteLocationViewModel: FavoriteLocationViewModel, activityScreenViewModel: ActivityScreenViewModel, homeScreenViewModel: HomeScreenViewModel) {
    val navController = rememberNavController()
    var isOnline by remember { mutableStateOf(true) }
    var showNoNetworkDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

     //this network observer will update the isOnline variable when the network status changes
    NetworkObserver { newStatus ->
        isOnline = newStatus
        showNoNetworkDialog = !newStatus
        Log.d("NetworkObserver", "Network status changed to $newStatus")
    }

    Scaffold(
        bottomBar = { if (!isLoading) { BottomNavigationBar(navController) } else null }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                LoadingScreen() // Show LoadingScreen instead of NavHost
                LaunchedEffect(Unit) { // Launch a coroutine to simulate loading
                    delay(1000L)
                    isLoading = false
                    if (!isOnline) {
                        showNoNetworkDialog = true
                    }
                }
            } else {
                // Show NavHost only when not loading.
                NavHost(navController, startDestination = "home") {
                    composable("home") { HomeScreen(isOnline, homeScreenViewModel) }
                    composable("activity") { ActivityScreen(isOnline, activityScreenViewModel) }
                    composable("location") { LocationScreen(isOnline, favoriteLocationViewModel) }
                }
                // Show NoNetworkDialog if isOnline is false
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