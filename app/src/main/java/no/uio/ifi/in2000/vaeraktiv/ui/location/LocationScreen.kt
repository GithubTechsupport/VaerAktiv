package no.uio.ifi.in2000.vaeraktiv.ui.location

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun LocationScreen() {
    val defaultPadding = 10.dp
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
            Brush.verticalGradient(
                colors = listOf(Color(0xFF6BAEDF), Color(0xFF8ACAFF))
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item { AddPlace(defaultPadding) }
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
        item { PlaceCard(
            "Trondheim",
            "cloudy",
            "Overskyet",
            "16",
            "9",
            "6",
            "0.4",
            "1",
            defaultPadding
        ) }
        item { PlaceCard(
            "Oslo",
            "clearsky_day",
            "Sol",
            "20",
            "16",
            "2",
            "0",
            "5",
            defaultPadding
        ) }
        item { PlaceCard(
            "Bergen",
            "heavyrain",
            "Mye regn",
            "10",
            "7",
            "2",
            "7",
            "5",
            defaultPadding
        ) }
    }
}
