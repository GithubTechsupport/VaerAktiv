package no.uio.ifi.in2000.vaeraktiv.ui.home.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.utils.IconMapper

/**
 * Displays a horizontal list of hourly weather forecasts.
 *
 * @param data List of hourly weather data.
 */

@Composable
fun DisplayHourlyForecast(data: List<ForecastForHour>) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(data.size) { index ->
            HourlyForecastItem(forecast = data[index])
        }
    }
}

/**
 * A box that displays weather data for one hour.
 *
 * @param forecast Hourly weather data.
 */
@Composable
private fun HourlyForecastItem(forecast: ForecastForHour) {
    val iconResId = IconMapper.fromName(forecast.icon?:"")

    Box(
        modifier = Modifier
            .width(80.dp)
            .height(160.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ForecastText(
                text = forecast.time ?: stringResource(R.string.n_a),
                style = MaterialTheme.typography.labelMedium
            )
            WeatherIcon(iconResId = iconResId)
            ForecastText(
                text = stringResource(R.string.temp, forecast.temp!!),
                style = MaterialTheme.typography.labelSmall
            )
            ForecastText(
                text = stringResource(R.string.nedb_r_ml, forecast.precipitationAmount!!),
                style = MaterialTheme.typography.labelSmall
            )
            ForecastText(
                text = stringResource(R.string.m_s, forecast.windSpeed!!),
                style = MaterialTheme.typography.labelSmall
            )
            ForecastText(
                text = stringResource(R.string.uv, forecast.uv!!),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/**
 * Text component for use in weather cards.
 *
 * @param text The text to display.
 * @param style Text style.
 */
@Composable
private fun ForecastText(text: String, style: TextStyle) {
    Text(
        text = text,
        style = style,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * Displays a weather icon based on resource ID. Shows a sun icon as fallback.
 *
 * @param iconResId Resource ID of the weather icon.
 */
@Composable
private fun WeatherIcon(iconResId: Int) {
    Image(
        painter = painterResource(id = if (iconResId != 0) iconResId else R.drawable.sun),
        contentDescription = "Weather icon",
        modifier = Modifier.size(40.dp)
    )
}