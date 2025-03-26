package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun DisplayWeather(temp: String, uv: String, rain: String, wind: String) {
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Erstatt "Ikon" med en faktisk ikon-composable senere
            Image(
                painter = painterResource(id = R.drawable.clearsky_day),
                contentDescription = "Ikon",
                modifier = Modifier.size(125.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "$temp°",
                style = MaterialTheme.typography.displayLarge, // Større tekst for hovedtemp
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Row {
            Text(
                text = "Stort sett overskyet",
                style = MaterialTheme.typography.titleMedium, // Mindre enn h2, mer lesbar
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .background(
                    color = Color(0xFFBCDEFD),
                    shape = RoundedCornerShape(cornerDp)
                )
                .fillMaxWidth()
                .padding(8.dp), // Legg til padding inni boksen
            horizontalArrangement = Arrangement.SpaceEvenly // Jevn fordeling
        ) {
            Text(
                text = "$uv UV",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = rain,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = wind,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}