package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import android.util.Log
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    isOnline: Boolean,
    viewModel: HomeScreenViewModel
) {
    val uiState by viewModel.homeScreenUiState.collectAsState()
    val currentLocation by viewModel.currentLocation.observeAsState()
    val deviceLocation by viewModel.deviceLocation.observeAsState()
    val activities by viewModel.activities.observeAsState()
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
        viewModel.initialize()
    }

    LaunchedEffect(Unit) {
        snapshotFlow { currentLocation?.addressName }
            .filterNotNull()
            .distinctUntilChanged()
            .collectLatest { newName ->
                if (newName != uiState.locationName) {
                    Log.d("HomeScreen", "Current location changed")
                    Log.d("HomeScreen", "New location: $newName, old location: ${uiState.locationName}")
                    viewModel.resetScreenState()
                }
            }
    }

    Box(Modifier
        .fillMaxSize()
        .pullRefresh(refreshState)
    ) {
        when {
            uiState.isLoading -> LoadingScreen()
            isOnline -> HomeContent(uiState, deviceLocation, activities, viewModel)
            else -> ErrorMessage("You're offline.")
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}