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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.MainActivity
import no.uio.ifi.in2000.vaeraktiv.model.navbar.NavbarUiState
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkObserver
import no.uio.ifi.in2000.vaeraktiv.ui.preferences.PreferencesViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.preferences.PreferencesScreen
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
    preferencesViewModel: PreferencesViewModel,
    navController: NavHostController
) {
    val bottomNavigationViewModel: BottomNavigationViewModel = viewModel()
    var uiState by remember { mutableStateOf(NavbarUiState()) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("VaerAktivPrefs", Context.MODE_PRIVATE)
    val startDestination = if (sharedPreferences.getBoolean("isOnboardingCompleted", false)) "home" else "welcome"

    // Set initial selected route based on start destination
    LaunchedEffect(startDestination) {
        bottomNavigationViewModel.updateSelectedRoute(startDestination)
    }

    Log.d("Navbar", "Recomposition triggered, current route: ${navController.currentDestination?.route}")

    // Observe network status
    NetworkObserver { isOnline ->
        uiState = uiState.copy(
            isOnline = isOnline,
            showNoNetworkDialog = !isOnline && !uiState.isOnboardingRoute
        )
    }

    // Handle navigation from ViewModels
    LaunchedEffect(preferencesViewModel.navigateBack) {
        preferencesViewModel.navigateBack.observeForever { shouldNavigate ->
            if (shouldNavigate) {
                handleNavigation(navController, uiState, "home")
                preferencesViewModel.onNavigationHandled()
                bottomNavigationViewModel.updateSelectedRoute("home")
            }
        }
    }

    LaunchedEffect(favoriteLocationViewModel.navigateToHome) {
        favoriteLocationViewModel.navigateToHome.observeForever { shouldNavigate ->
            if (shouldNavigate) {
                uiState = handleNavigation(navController, uiState, "home")
                favoriteLocationViewModel.onNavigationHandled()
                bottomNavigationViewModel.updateSelectedRoute("home")
            }
        }
    }

    LaunchedEffect(homeScreenViewModel.navigateToPreferences) {
        homeScreenViewModel.navigateToPreferences.observeForever { shouldNavigate ->
            if (shouldNavigate) {
                handleNavigation(navController, uiState, "preferences")
                homeScreenViewModel.onNavigationHandled()
            }
        }
    }

    LaunchedEffect(homeScreenViewModel.navigateToMap) {
        homeScreenViewModel.navigateToMap.collect { activity ->
            uiState = handleNavigation(navController, uiState, "map")
            mapScreenViewModel.zoomInOnActivity(activity)
            bottomNavigationViewModel.updateSelectedRoute("map")
        }
    }

    // Determine route states
    val currentRoute by navController.currentBackStackEntryAsState()
    val routeInfo = getRouteInfo(currentRoute?.destination?.route)

    Scaffold(
        bottomBar = {
            if (!uiState.isLoading && routeInfo.showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    viewModel = bottomNavigationViewModel
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
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
                            bottomNavigationViewModel.updateSelectedRoute("home")
                        }
                    )
                }
                composable("home") { HomeScreen(
                    isOnline = uiState.isOnline,
                    viewModel = homeScreenViewModel,
                    navController = navController
                ) }
                composable("preferences") {
                    PreferencesScreen(preferencesViewModel)
                }
                composable("location") { LocationScreen(uiState.isOnline, favoriteLocationViewModel) }
                composable("map") { MapScreen(mapScreenViewModel) }
            }

            if (uiState.isLoading) {
                LoadingScreen()
                LaunchedEffect(Unit) {
                    delay(1000L)
                    uiState = uiState.copy(
                        isLoading = false,
                        showNoNetworkDialog = !uiState.isOnline && !routeInfo.isOnboardingRoute
                    )
                }
            } else if (uiState.showNoNetworkDialog && !routeInfo.isOnboardingRoute) {
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

// Helper function to determine route information
private data class RouteInfo(val showBottomBar: Boolean, val isOnboardingRoute: Boolean)

private fun getRouteInfo(route: String?): RouteInfo {
    val onboardingRoutes = listOf("welcome", "onboarding", "final_onboarding")
    val isOnboarding = route in onboardingRoutes
    return RouteInfo(
        showBottomBar = route != null && !isOnboarding,
        isOnboardingRoute = isOnboarding
    )
}

// Helper function to handle navigation and state update
private fun handleNavigation(
    navController: NavController,
    uiState: NavbarUiState,
    route: String
): NavbarUiState {
    when (route) {
        "home" -> navController.navigateToHome()
        "map" -> navController.navigateToMap()
        "preferences" -> navController.navigateToPreferences()
    }
    return uiState.copy(selectedRoute = route)
}

// Extension functions for navigation
private fun NavController.navigateToHome(popUpTo: String? = null) {
    navigate("home") {
        popUpTo?.let { popUpTo(it) { inclusive = true } }
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

private fun NavController.navigateToPreferences(popUpTo: String? = null) {
    navigate("preferences") {
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