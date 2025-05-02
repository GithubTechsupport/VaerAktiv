package no.uio.ifi.in2000.vaeraktiv.ui.navbar
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkObserver
import no.uio.ifi.in2000.vaeraktiv.ui.activity.PreferencesViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.activity.SettingsScreen
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreen
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.LocationScreen
import no.uio.ifi.in2000.vaeraktiv.ui.map.MapScreen
import no.uio.ifi.in2000.vaeraktiv.ui.map.MapScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.welcome.InfoPeferencesScreen
import no.uio.ifi.in2000.vaeraktiv.ui.welcome.InformationScreen
import no.uio.ifi.in2000.vaeraktiv.ui.welcome.WelcomeScreen


/*
*
* This function is the navbar. This function is called in MainActivity.kt. When this function is called a display in the button wil be called, this display will
*  have tre buttons called from the ButtonNavigationBar function.
*The function also observe and update if the network status changes on the device. If the internet connection is lost a dialog will be displayed. if not the navbar is shown
* */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navbar(
    favoriteLocationViewModel: FavoriteLocationViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    mapScreenViewModel: MapScreenViewModel,
    preferencesViewModel: PreferencesViewModel
) {
    val navController = rememberNavController()
    var isOnline by remember { mutableStateOf(true) }
    var showNoNetworkDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedRoute by remember { mutableStateOf("home") }

    // Check if onboarding is completed
    val sharedPreferences = context.getSharedPreferences("VaerAktivPrefs", Context.MODE_PRIVATE)
    val isOnboardingCompleted = sharedPreferences.getBoolean("isOnboardingCompleted", false)

    // Log onboarding status for debugging
    LaunchedEffect(isOnboardingCompleted) {
        Log.d("Navbar", "isOnboardingCompleted: $isOnboardingCompleted")
    }

    // Observe navigation from FavoriteLocationViewModel
    LaunchedEffect(favoriteLocationViewModel.navigateToHome) {
        favoriteLocationViewModel.navigateToHome.observeForever { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigate("home") {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
                favoriteLocationViewModel.onNavigationHandled()
                selectedRoute = "home"
            }
        }
    }

    LaunchedEffect(homeScreenViewModel.navigateToMap) {
        homeScreenViewModel.navigateToMap.collect { activity ->
            // navigate to map
            navController.navigate("map") {
                popUpTo(navController.graph.startDestinationId); launchSingleTop = true
            }
            selectedRoute = "map"
            mapScreenViewModel.zoomInOnActivity(activity)
        }
    }

     //this network observer will update the isOnline variable when the network status changes

    NetworkObserver { newStatus ->
        isOnline = newStatus
        showNoNetworkDialog = !newStatus
        Log.d("NetworkObserver", "Network status changed to $newStatus")
    }

    // Determine if bottom bar should be shown (hide for welcome, onboarding, final_onboarding)
    val currentRoute by navController.currentBackStackEntryAsState()
    val showBottomBar = currentRoute?.destination?.route !in listOf("welcome", "onboarding", "final_onboarding")
    val isOnboardingRoute = currentRoute?.destination?.route in listOf("welcome", "onboarding", "final_onboarding")

    Scaffold(
        bottomBar = {
            if (!isLoading && showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    getSelectedRoute = { selectedRoute },
                    setSelectedRoute = { newValue -> selectedRoute = newValue }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLoading) {
                LoadingScreen()
                LaunchedEffect(Unit) {
                    delay(1000L)
                    isLoading = false
                    if (!isOnline && !isOnboardingRoute) {
                        showNoNetworkDialog = true
                    }
                }
            } else {
                NavHost(
                    navController = navController,
                    startDestination = if (isOnboardingCompleted) "home" else "welcome"
                ) {
                    composable("welcome") {
                        WelcomeScreen(
                            onStartClick = { navController.navigate("onboarding") }
                        )
                    }
                    composable("onboarding") {
                        InfoPeferencesScreen(
                            viewModel = preferencesViewModel,
                            onContinueClick = { navController.navigate("final_onboarding") }
                        )
                    }
                    composable("final_onboarding") {
                        InformationScreen(
                            onStartApplication = {
                                // Mark onboarding as completed
                                sharedPreferences.edit()
                                    .putBoolean("isOnboardingCompleted", true)
                                    .apply()
                                Log.d("Navbar", "Onboarding completed, set isOnboardingCompleted to true")

                                // Navigate to home
                                navController.navigate("home") {
                                    popUpTo("welcome") { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable("home") { HomeScreen(isOnline, homeScreenViewModel) }
                    composable("settings") { SettingsScreen(preferencesViewModel) }
                    composable("location") { LocationScreen(isOnline, favoriteLocationViewModel) }
                    composable("map") { MapScreen(mapScreenViewModel) }
                }
                if (showNoNetworkDialog && !isOnboardingRoute) {
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