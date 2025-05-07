package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(isOnline: Boolean, viewModel: HomeScreenViewModel) { // Manifest.xml. Ternger kanskje ikke resizable linjen
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val deviceLocation: (Location) -> Unit = { viewModel.setCurrentLocation(it) }
    val activities by viewModel.activities.observeAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    var isRefreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState (
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.getHomeScreenData()
            isRefreshing = false
        }
    )

    LaunchedEffect(Unit) {
        viewModel.initialize(lifecycleOwner)
    }

    LaunchedEffect(currentLocation) {
        currentLocation
            ?.takeIf { it.addressName != uiState.locationName }
            ?.let { viewModel.resetScreenState() }
    }

    Box(Modifier
        .fillMaxSize()
        .pullRefresh(refreshState)
    ) {
        when {
            uiState.isLoading -> LoadingScreen()
            isOnline    -> HomeContent(uiState, viewModel.currentLocation.value, { viewModel.setCurrentLocation(it) }, viewModel.activities.value, viewModel)
            else        -> ErrorMessage("You're offline.")
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}




