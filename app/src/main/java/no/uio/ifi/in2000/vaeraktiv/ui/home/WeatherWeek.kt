package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

data class WeatherDay(
    val date: String,      // Dato (f.eks. "27. mars")
    val maxTemp: Int,      // Maksimumstemperatur i °C
    val icon: String       // Placeholder-ikon (f.eks. "☀️"), erstattes senere
)

// Dummydata for 8 dager (26. mars 2025 og fremover)
val dummyWeatherData = listOf(
    WeatherDay(date = "26. mars", maxTemp = 8, icon = "clearsky_day"),
    WeatherDay(date = "27. mars", maxTemp = 10, icon = "cloudy"),
    WeatherDay(date = "28. mars", maxTemp = 7, icon = "fog"),
    WeatherDay(date = "29. mars", maxTemp = 6, icon = "fog"),
    WeatherDay(date = "30. mars", maxTemp = 9, icon = "snow"),
    WeatherDay(date = "31. mars", maxTemp = 11, icon = "snow"),
    WeatherDay(date = "1. april", maxTemp = 12, icon = "fog"),
    WeatherDay(date = "2. april", maxTemp = 10, icon = "snow")
)

@Composable
fun WeatherWeek(data: List<WeatherDay>) {
    val cornerDp = 10.dp
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = Color(0xFFBCDEFD),
                shape = RoundedCornerShape(cornerDp))
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "7-dagersvarsel",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(modifier = Modifier
            .width(150.dp)
            .padding(top = 4.dp)
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.onBackground)
        )
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, // jevn fordeling
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(
                text = "Dato",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Max Temp",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Dagens vær",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        Box(modifier = Modifier
            .width(200.dp)
            .padding(top = 4.dp)
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        )
        data.take(7).forEach { day ->
            var isExpanded by remember { mutableStateOf(false) }
            val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween, // jevn fordeling
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp) // vertikal avstand mellom radene
                    .clickable { isExpanded = !isExpanded }
            ) {
                Text(
                    text = day.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${day.maxTemp}°",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                )
                //Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = if (iconResId != 0) iconResId else R.drawable.sun),
                    contentDescription = "Dagens vær",
                    modifier = Modifier
                        .size(30.dp)
                        .weight(1f)
                )
            }
            if (isExpanded) {
                Row {
                    Text(
                        text = "3 UV",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.weight(1f)

                    )
                    Text(
                        text = "Nedbør mm",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Vind m/s",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Box(modifier = Modifier
                .width(200.dp)
                .padding(top = 4.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
    }
}