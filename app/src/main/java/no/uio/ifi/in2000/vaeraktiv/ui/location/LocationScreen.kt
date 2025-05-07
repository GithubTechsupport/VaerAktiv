package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LocationScreen(isOnline: Boolean, viewModel: FavoriteLocationViewModel) {
    if(isOnline) {
        val defaultPadding = 8.dp

        val list by viewModel.data.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 0.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Text(
                    "Steder",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            item { AddPlace(defaultPadding, viewModel) }
            list.forEach {
                item {
                    PlaceCard(it, defaultPadding, viewModel)
                }
            }
        }
    }
}
