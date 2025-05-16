package no.uio.ifi.in2000.vaeraktiv.ui.home.weather

import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenUiState
import no.uio.ifi.in2000.vaeraktiv.utils.IconMapper

/**
 * Composable function that displays today's weather with the option to expand for more details.
 *
 * @param data Weather data for the day.
 * @param uiState UI state containing errors and hourly forecasts.
 */

@Composable
fun DisplayWeather(data: ForecastToday?, uiState: HomeScreenUiState) {
    val iconResId = IconMapper.fromName(data?.iconNow?:"")
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Spacer(modifier = Modifier.height(12.dp))

    // Main container with clickable expansion toggle
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { expanded = !expanded }
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(10.dp),
            )
    ) {
        // Row displaying weather icon and current temp + UV index
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Weather icon",
                    modifier = Modifier.size(145.dp)
                )
            }
            Column (
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.temp, data?.tempNow?: 0.0),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.uv, data?.uv ?: 0.0),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Expanded details shown on click
        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Use the same overall padding as above
            ) {
                Row {
                    // Display low temperature
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.L_temp, data?.tempMin?: 0.0),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                    // Display high temperature
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.H_temp, data?.tempMax?: 0.0),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                    // Display precipitation amount
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.ml, data?.precipitationAmount?: 0.0),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                    // Display wind speed
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.m_s, data?.windSpeed?: 0.0),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Show error or hourly forecast
                uiState.next24HoursError?.let {
                    ErrorMessage(
                        message = "Error fetching today's weather: ${uiState.next24HoursError}"
                    )
                } ?: run {
                    Log.d("DisplayHourlyForecast", "Forecast: ${uiState.next24Hours}")
                    DisplayHourlyForecast(uiState.next24Hours)
                }

                // Show error or sunrise/sunset info
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

        // Expand/collapse indicator row
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ){
            Text(
                text = stringResource(R.string.detaljer),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}