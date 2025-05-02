package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import android.content.Context
import android.os.Build
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.model.navbar.NavbarUiState
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
import androidx.core.content.edit


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navbar(
    favoriteLocationViewModel: FavoriteLocationViewModel,
    homeScreenViewModel: HomeScreenViewModel,
    mapScreenViewModel: MapScreenViewModel,
    preferencesViewModel: PreferencesViewModel
) {
    val navController = rememberNavController()
    var uiState by remember { mutableStateOf(NavbarUiState()) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("VaerAktivPrefs", Context.MODE_PRIVATE)
    val startDestination = if (sharedPreferences.getBoolean("isOnboardingCompleted", false)) "home" else "welcome"

    // Observe network status
    NetworkObserver { isOnline ->
        uiState = uiState.copy(
            isOnline = isOnline,
            showNoNetworkDialog = !isOnline && !uiState.isOnboardingRoute
        )
    }

    // Handle navigation from ViewModels
    LaunchedEffect(favoriteLocationViewModel.navigateToHome) {
        favoriteLocationViewModel.navigateToHome.observeForever { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigateToHome()
                favoriteLocationViewModel.onNavigationHandled()
                uiState = uiState.copy(selectedRoute = "home")
            }
        }
    }

    LaunchedEffect(homeScreenViewModel.navigateToMap) {
        homeScreenViewModel.navigateToMap.collect { activity ->
            navController.navigateToMap()
            uiState = uiState.copy(selectedRoute = "map")
            mapScreenViewModel.zoomInOnActivity(activity)
        }
    }

    // Determine if bottom bar should be shown
    val currentRoute by navController.currentBackStackEntryAsState()
    val showBottomBar = currentRoute?.destination?.route?.let { route ->
        route !in listOf("welcome", "onboarding", "final_onboarding")
    } == true
    uiState = uiState.copy(isOnboardingRoute = currentRoute?.destination?.route in listOf("welcome", "onboarding", "final_onboarding"))

    Scaffold(
        bottomBar = {
            if (!uiState.isLoading && showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    getSelectedRoute = { uiState.selectedRoute },
                    setSelectedRoute = { uiState = uiState.copy(selectedRoute = it) }
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                    LaunchedEffect(Unit) {
                        delay(1000L)
                        uiState = uiState.copy(
                            isLoading = false,
                            showNoNetworkDialog = !uiState.isOnline && !uiState.isOnboardingRoute
                        )
                    }
                }
                else -> {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("welcome") {
                            WelcomeScreen(onStartClick = { navController.navigate("onboarding") })
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
                                    sharedPreferences.edit {
                                        putBoolean("isOnboardingCompleted", true)
                                    }
                                    navController.navigateToHome(popUpTo = "welcome")
                                }
                            )
                        }
                        composable("home") { HomeScreen(uiState.isOnline, homeScreenViewModel) }
                        composable("settings") { SettingsScreen(preferencesViewModel) }
                        composable("location") { LocationScreen(uiState.isOnline, favoriteLocationViewModel) }
                        composable("map") { MapScreen(mapScreenViewModel) }
                    }
                    if (uiState.showNoNetworkDialog && !uiState.isOnboardingRoute) {
                        NoNetworkDialog(
                            onRetry = {
                                uiState = uiState.copy(isLoading = true, showNoNetworkDialog = false)
                            },
                            onClose = { (context as? MainActivity)?.finishAffinity() }
                        )
                    }
                }
            }
        }
    }
}

// Extension functions for navigation
private fun NavController.navigateToHome(popUpTo: String? = null) {
    navigate("home") {
        popUpTo?.let { popUpTo(it) { inclusive = true } }
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

private fun NavController.navigateToMap() {
    navigate("map") {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}