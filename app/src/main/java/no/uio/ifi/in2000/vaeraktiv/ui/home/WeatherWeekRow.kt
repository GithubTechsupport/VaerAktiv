package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForDay

/**
 * A single row in the weekly weather forecast, showing the day name, max temperature,
 * weather icon, and an expand/collapse indicator.
 *
 * Tapping the row triggers the onClick callback to expand or collapse additional details.
 *
 * @param day The ForecastForDay object containing date and temperature.
 * @param iconResId A string representing the name of the drawable resource for the weather icon.
 * @param context The Context used to resolve the drawable resource dynamically.
 * @param expanded A Boolean indicating whether the row is currently expanded.
 * @param onClick Callback function invoked when the row is clicked.
 */
@SuppressLint("DiscouragedApi")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherWeekRow(
    day: ForecastForDay,
    iconResId: String,
    context: Context,
    expanded: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        // Display the day of the week
        Text(
            text = getDayOfWeek(day.date),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.width(80.dp)
        )

        Spacer(modifier = Modifier.weight(0.8f))

        // Show max temperature for the day
        Text(
            text = stringResource(R.string.temp, day.maxTemp),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.wrapContentWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        // Weather icon and expand/collapse arrow
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
        ) {
            // Weather icon resolved at runtime
            Image(
                painter = painterResource(
                    id = context.resources.getIdentifier(iconResId, "drawable", context.packageName)
                ),
                contentDescription = "Today's weather",
                modifier = Modifier.size(40.dp)
            )

            // Expand/collapse icon
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Expand/Collapse",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}
