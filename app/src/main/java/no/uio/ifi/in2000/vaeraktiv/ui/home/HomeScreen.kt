package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    isOnline: Boolean,
    viewModel: HomeScreenViewModel,
    navController: NavHostController
) { // Manifest.xml. Ternger kanskje ikke resizable linjen
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val deviceLocation by viewModel.deviceLocation.observeAsState()
    val activities by viewModel.activities.observeAsState()
    Log.d("HomeScreen", "Devicelocation: $deviceLocation")
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        viewModel.initialize(lifecycleOwner)
    }

    LaunchedEffect(currentLocation) {
        currentLocation?.let { viewModel.getHomeScreenData() }
    }

    when {
        uiState.isLoading -> LoadingScreen()
        isOnline -> HomeContent(uiState, deviceLocation, activities, viewModel, navController)
        else -> ErrorMessage("You're offline.")
    }
}