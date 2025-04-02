package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.nowcast.Details
import no.uio.ifi.in2000.vaeraktiv.model.ui.TodaysWeatherData


@SuppressLint("DiscouragedApi")
@Composable
fun DisplayWeather(data : TodaysWeatherData) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data.iconDescNow, "drawable", context.packageName)
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
                painter = painterResource(id = iconResId),
                contentDescription = "Ikon",
                modifier = Modifier.size(125.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = "${data.tempNow}°",
                style = MaterialTheme.typography.displayLarge, // Større tekst for hovedtemp
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Row {
            Text(
                text = "Stort sett overskyet", // Kanskje lage en when for å sette riktig tekst etter værtype senere
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
                .padding(20.dp), // Legg til padding inni boksen
            horizontalArrangement = Arrangement.SpaceEvenly // Jevn fordeling
        ) {
            Text(
                text = "${data.uv} UV",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${data.precipitationAmount} mm",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "${data.wind} m/s",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}