package no.uio.ifi.in2000.vaeraktiv.ui.map

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen

@Composable
fun MapScreen(viewModel: MapScreenViewModel) {

    val uiState by viewModel.mapScreenUiState.collectAsState()
    val activities by viewModel.activities.observeAsState()

    LaunchedEffect(key1 = activities) {
        activities?.get(0)?.let { viewModel.updatePlacesAndRoutes(it) }
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        OsmMapView(
            context = LocalContext.current,
            places = uiState.places,
            routes = uiState.routes
        )
    }
}