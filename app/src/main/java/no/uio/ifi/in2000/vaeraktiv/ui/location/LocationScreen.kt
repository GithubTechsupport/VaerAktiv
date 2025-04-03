package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainBackground
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryBackground

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun LocationScreen(isOnline: Boolean, viewModel: FavoriteLocationViewModel) {
    if(isOnline) {
        val defaultPadding = 10.dp

        val list by viewModel.data.collectAsState(initial = emptyList())
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(MainBackground, SecondaryBackground)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item { AddPlace(defaultPadding, viewModel) }
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(defaultPadding)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground)
                )
            }
            /* TODO: Legge til en liste man sender inn i AddPlace som man fyller opp
            *  Denne listen skal man kjøre en forEach{} på sånn at alle stedene får et PlaceCard*/


            list.forEach {
                item {
                    PlaceCard(it, defaultPadding, viewModel)
                }
            }
        }
    }
}
