package no.uio.ifi.in2000.vaeraktiv.ui.location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LocationScreen(isOnline: Boolean, viewModel: FavoriteLocationViewModel) {
    if(isOnline) {
        val list by viewModel.data.collectAsState(initial = emptyList())

        var isRefreshing by remember { mutableStateOf(false) }
        val refreshState = rememberPullRefreshState(
            refreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.getData()
                isRefreshing = false
            }
        )

        Box(Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp)
        ) {
            // LazyColumn for displaying the list of favorite locations
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        text = stringResource(R.string.steder),
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                item { AddPlace(8.dp, viewModel) }
                // Display each favorite location using PlaceCard
                items(list) {
                    PlaceCard(it, 8.dp, viewModel)
                }
            }
            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
