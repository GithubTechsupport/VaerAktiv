package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage

/**
 * Composable function that displays today's weather with the option to expand for more details.
 *
 * @param data Weather data for the day.
 * @param uiState UI state containing errors and hourly forecasts.
 */

@SuppressLint("DiscouragedApi")
@Composable
fun DisplayWeather(data: ForecastToday?, uiState: HomeScreenUiState) {
    if (data == null) {
        ErrorMessage(message = "Ingen værdata tilgjengelig")
        return
    }
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data.iconNow, "drawable", context.packageName)
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
        WeatherIconAndTemp(data, iconResId)
        // Expanded details shown on click
        if (expanded) {
            ExpandedWeatherDetails(data)
            Spacer(modifier = Modifier.height(8.dp))
            // Show error or hourly forecast
            uiState.next24HoursError?.let {
                ErrorMessage(message = "Error fetching today's weather: ${uiState.next24HoursError}")
            } ?: DisplayHourlyForecast(uiState.next24Hours)
            // Show error or sunrise/sunset info
            uiState.sunRiseSetError?.let {
                ErrorMessage(message = "Error fetching today's weather: ${uiState.sunRiseSetError}")
            } ?: SunRiseSet(uiState.sunRiseSet)
        }
        // Expand/collapse indicator row
        ExpandedIndicator(expanded)
        Spacer(modifier = Modifier.height(8.dp))
    }
}