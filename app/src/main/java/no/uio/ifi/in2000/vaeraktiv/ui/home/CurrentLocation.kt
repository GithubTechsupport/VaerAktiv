package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainCard
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryCard

@Composable
fun CurrentLocation(locationName : String) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(8.dp)
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.location),
                contentDescription = "Location Icon",
                modifier = Modifier
                    .size(45.dp)
                    .padding(end = 2.dp)
            )
            Text(
                text = locationName,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = 40.sp
            )
        }
        Box(modifier = Modifier
            .width(150.dp)
            .padding(top = 4.dp)
            .height(1.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(MainCard, SecondaryCard)
                )
            )
        )
    }
}