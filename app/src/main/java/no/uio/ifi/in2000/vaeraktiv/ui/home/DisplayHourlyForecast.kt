package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunEvent
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun DisplayHourlyForecast(data: List<ForecastForHour>, sunData: List<String>) {
    val sunRise = sunData.getOrNull(0) ?: "N/A"
    val sunSet = sunData.getOrNull(1) ?: "N/A"
    val cornerDp = 10.dp
    val context = LocalContext.current
    val sunRiseHour = if (sunRise != "N/A") sunRise.split(":").first().padStart(2, '0') else null
    val sunSetHour = if (sunSet != "N/A") sunSet.split(":").first().padStart(2, '0') else null


    val items = mutableListOf<Any>().apply {
        addAll(data)

        if (sunRiseHour != null && sunRiseHour > data[0].time.toString()) {
            val insertIndex = data.indexOfFirst { it.time == sunRiseHour } + 1
            add(insertIndex, SunDisplayData(sunRiseHour, sunRise, "Sol opp", "sunrise_color3"))
        }
        if (sunSetHour != null && sunSetHour > data[0].time.toString()) {
            val insertIndex = data.indexOfFirst { it.time == sunSetHour } + 1
            add(insertIndex, SunDisplayData(sunSetHour, sunSet, "Sol ned", "sunset_color"))
        }
    }
    Column {
        LazyRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items.size) { index ->
                val item = items[index]
                when (item) {
                    is ForecastForHour -> {
                        val iconResId = context.resources.getIdentifier(item.icon, "drawable", context.packageName)
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(80.dp)
                                .height(160.dp)
                                .background(
                                    BackGroundColor,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, OnContainer, shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = item.time ?: "N/A",
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center,
                                    color = OnContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Image(
                                    painter = painterResource(id = if (iconResId != 0) iconResId else R.drawable.sun),
                                    contentDescription = "Weather icon",
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${item.temp}°C", // Assumes you have `temperature` in ForecastForHour
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    color = OnContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${item.precipitationAmount}mm", // Assumes you have `temperature` in ForecastForHour
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    color = OnContainer
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "${item.windSpeed}m/s", // Assumes you have `temperature` in ForecastForHour
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center,
                                    color = OnContainer
                                )
                            }
                        }
                    }
                    is SunDisplayData -> {
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .width(80.dp)
                                .height(160.dp)
                                .background(
                                    color = BackGroundColor,
                                    shape = RoundedCornerShape(cornerDp),
                                )
                                .border(1.dp, OnContainer, RoundedCornerShape(cornerDp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.fullTime,
                                    style = MaterialTheme.typography.labelMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Image(
                                    painter = painterResource(id = context.resources.getIdentifier(item.icon, "drawable", context.packageName)),
                                    contentDescription = "Sunrise icon",
                                    modifier = Modifier.size(50.dp)
                                )
                                Spacer(modifier = Modifier.height(25.dp))
                                Text(
                                    text = item.type,
                                    style = MaterialTheme.typography.labelSmall,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(25.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
data class SunDisplayData(
    val hour: String,
    val fullTime: String,
    val type: String,
    val icon: String
)


