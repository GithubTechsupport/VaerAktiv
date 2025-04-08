package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainCard
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryCard

val dummyForecastData = listOf(
    ForecastToday(
        tempNow = "12°C",
        tempMax = "15°C",
        tempMin = "8°C",
        uv = "3",
        windSpeed = "5 m/s",
        precipitationAmount = "0.2 mm",
        icon = "partly_cloudy_day",
        iconNow = "cloudy"
    ),
    ForecastToday(
        tempNow = "18°C",
        tempMax = "22°C",
        tempMin = "14°C",
        uv = "5",
        windSpeed = "3 m/s",
        precipitationAmount = "0 mm",
        icon = "sunny",
        iconNow = "clear"
    ),
    ForecastToday(
        tempNow = "7°C",
        tempMax = "10°C",
        tempMin = "4°C",
        uv = "2",
        windSpeed = "8 m/s",
        precipitationAmount = "1.5 mm",
        icon = "rain",
        iconNow = "light_rain"
    ),
    ForecastToday(
        tempNow = "25°C",
        tempMax = "28°C",
        tempMin = "20°C",
        uv = "7",
        windSpeed = "4 m/s",
        precipitationAmount = "0 mm",
        icon = "sunny",
        iconNow = "clear"
    ),
    ForecastToday(
        tempNow = "-2°C",
        tempMax = "1°C",
        tempMin = "-5°C",
        uv = "1",
        windSpeed = "6 m/s",
        precipitationAmount = "0.5 mm",
        icon = "snow",
        iconNow = "light_snow"
    )
    ,
    ForecastToday(
        tempNow = "-2°C",
        tempMax = "1°C",
        tempMin = "-5°C",
        uv = "1",
        windSpeed = "6 m/s",
        precipitationAmount = "0.5 mm",
        icon = "snow",
        iconNow = "light_snow"
    ),
    ForecastToday(
        tempNow = "-2°C",
        tempMax = "1°C",
        tempMin = "-5°C",
        uv = "1",
        windSpeed = "6 m/s",
        precipitationAmount = "0.5 mm",
        icon = "snow",
        iconNow = "light_snow"
    ),
    ForecastToday(
        tempNow = "-2°C",
        tempMax = "1°C",
        tempMin = "-5°C",
        uv = "1",
        windSpeed = "6 m/s",
        precipitationAmount = "0.5 mm",
        icon = "snow",
        iconNow = "light_snow"
    ),
    ForecastToday(
        tempNow = "-2°C",
        tempMax = "1°C",
        tempMin = "-5°C",
        uv = "1",
        windSpeed = "6 m/s",
        precipitationAmount = "0.5 mm",
        icon = "snow",
        iconNow = "light_snow"
    )
)

@Preview
@Composable
fun TodaysWeatherHourByHourPreview() {
    TodaysWeatherHourByHour(dummyForecastData)
}

@Composable
fun TodaysWeatherHourByHour(data: List<ForecastToday>) {
    val cornerDp = 10.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(MainCard, SecondaryCard)
                ),
                shape = RoundedCornerShape(cornerDp)
            )
            .fillMaxSize()
            .padding(8.dp)
    ){
        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            items(data) { forecast ->
                DisplayThisHour(data = forecast)
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun DisplayThisHour(data: ForecastToday) {
    //val context = LocalContext.current
    //val iconResId = context.resources.getIdentifier(data.icon ?: "fog", "drawable", context.packageName).takeIf { it != 0 } ?: R.drawable.fog
    val isExpanded by remember { mutableStateOf(false) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(MainCard, SecondaryCard)
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { isExpanded }
    ){
        Text("Time")
        Spacer(modifier = Modifier.height(0.dp))
        Image(
            painter = painterResource(id = R.drawable.snow),
            contentDescription = "Dagens vær",
            modifier = Modifier.size(45.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(data.tempNow ?: "N/A")
    }
    if (isExpanded) {

    }
}
