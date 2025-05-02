package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun DisplayWeather(data: ForecastToday?, uiState: HomeScreenUiState) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data?.iconNow, "drawable", context.packageName)
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable (
                interactionSource = interactionSource,
                indication = null
            ){ expanded = !expanded }
            .background(
                Container,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp).padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Icon cell
            Box(
                modifier = Modifier,
                    //.weight(1f)
                    //.padding(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Ikon",
                    modifier = Modifier.size(145.dp)
                )
            }
            // Temperature cell and uv
            Column (
                modifier = Modifier,
                    //.weight(1f),
                    //.padding(end = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${data?.tempNow}°",
                    style = MaterialTheme.typography.displayMedium,
                    color = OnContainer,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${data?.uv} UV",
                    style = MaterialTheme.typography.headlineMedium,
                    color = OnContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (expanded) {
            //Spacer(modifier = Modifier.height(4.dp))
            // Second row: UV, precipitation, and wind speed details in a background box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Use the same overall padding as above
            ) {
                Row {
                    // UV details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "L: ${data?.tempMin}°",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                    // Precipitation details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "H: ${data?.tempMax}°",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${data?.precipitationAmount} mm",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                    // Wind speed details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${data?.windSpeed} m/s",
                            style = MaterialTheme.typography.titleMedium,
                            color = OnContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                uiState.next24HoursError?.let {
                    ErrorMessage(
                        message = "Error fetching today's weather: ${uiState.next24HoursError}"
                    )
                } ?: run {
                    Log.d("DisplayHourlyForecast", "Forecast: ${uiState.next24Hours}")
                    DisplayHourlyForecast(uiState.next24Hours)
                }

                uiState.sunRiseSetError?.let {
                    ErrorMessage(
                        message = "Error fetching today's weather: ${uiState.sunRiseSetError}"
                    )
                } ?: run {
                    Log.d("DisplayHourlyForecast", "Forecast: ${uiState.sunRiseSet}")
                    SunRiseSet(uiState.sunRiseSet)
                }
            }
        }
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = "Detaljer",
                style = MaterialTheme.typography.labelLarge,
                color = OnContainer,
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}