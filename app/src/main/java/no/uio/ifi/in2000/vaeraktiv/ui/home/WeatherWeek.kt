package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay

@RequiresApi(Build.VERSION_CODES.O)
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
                color = MaterialTheme.colorScheme.inversePrimary,
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
        data.take(7).forEach { day ->
            //var isExpanded by remember { mutableStateOf(false) }
            val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = getDayOfWeek(day.date),
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

@RequiresApi(Build.VERSION_CODES.O)
private fun getDayOfWeek(date: String): String {
    return try {
        val localDate = java.time.LocalDate.parse(date)
        val dayOfWeek = localDate.dayOfWeek
        when (dayOfWeek!!) {
            java.time.DayOfWeek.MONDAY -> "Mandag"
            java.time.DayOfWeek.TUESDAY -> "Tirsdag"
            java.time.DayOfWeek.WEDNESDAY -> "Onsdag"
            java.time.DayOfWeek.THURSDAY -> "Torsdag"
            java.time.DayOfWeek.FRIDAY -> "Freadg"
            java.time.DayOfWeek.SATURDAY -> "Lørdag"
            java.time.DayOfWeek.SUNDAY -> "Søndag"
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        "error"
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