package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainCard
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryCard
import no.uio.ifi.in2000.vaeraktiv.model.ui.ThisWeeksWeatherData


val dummyWeatherData = listOf(
    ForecastForDay(date = "26. mars", maxTemp = "8", null, iconDesc = "clearsky_day", null, null),
    ForecastForDay(date = "27. mars", maxTemp = "10", null, iconDesc = "cloudy", null, null),
    ForecastForDay(date = "28. mars", maxTemp = "12", null, iconDesc = "rain", null, null),
    ForecastForDay(date = "29. mars", maxTemp = "14", null, iconDesc = "rain", null, null),
    ForecastForDay(date = "30. mars", maxTemp = "16", null, iconDesc = "rain", null, null),
    ForecastForDay(date = "31. mars", maxTemp = "18", null, iconDesc = "rain", null, null),
    ForecastForDay(date = "1. april", maxTemp = "20", null, iconDesc = "rain", null, null),
    ForecastForDay(date = "2. april", maxTemp = "22", null, iconDesc = "rain", null, null)
)
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay

@SuppressLint("DiscouragedApi")
@Composable
fun WeatherWeek(data: List<ForecastForDay>) {

    val cornerDp = 10.dp
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
              Brush.verticalGradient(
                colors = listOf(MainCard, SecondaryCard)
                ),
                shape = RoundedCornerShape(cornerDp)
            )
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "7-dagersvarsel",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .width(150.dp)
                .padding(top = 4.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Dato",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Maks Temp",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Dagens Vær",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = Modifier
                .width(200.dp)
                .padding(top = 4.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        )
        data.forEach { day ->
            var isExpanded by remember { mutableStateOf(false) }
            val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = day.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f) // Tar tilgjengelig plass til venstre
                )
                Spacer(modifier = Modifier.weight(0.5f))

                Text(
                    text = "${day.maxTemp}°",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth() // Tar kun nødvendig bredde for å være sentrert
                )
                Spacer(modifier = Modifier.weight(1.1f)) // Fyller tomrommet til høyre
                Image(
                    painter = painterResource(id = if (iconResId != 0) iconResId else R.drawable.sun),
                    contentDescription = "Dagens vær",
                    modifier = Modifier
                        .size(40.dp)
                        .wrapContentWidth(align = Alignment.End) // Plasserer bildet helt til høyre
                )
            }
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 4.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
    }
}
/*if (isExpanded) {
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
            }*/