package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.utils.IconMapper

/**
 * Displays weather icons for different times throughout the day in a horizontal row.
 *
 * @param data List of detailed weather forecasts for one day.
 */
@Composable
fun DisplayIntervalSymbols(data: List<DetailedForecastForDay>) {
    val spacing = 4.dp

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacing)
    ) {
        data.forEach { forecast ->
            val iconResId = IconMapper.fromName(forecast.icon ?: stringResource(R.string.n_a))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = forecast.interval,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Image(
                        painter = painterResource(id = iconResId),
                        contentDescription = "Weather icon",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

