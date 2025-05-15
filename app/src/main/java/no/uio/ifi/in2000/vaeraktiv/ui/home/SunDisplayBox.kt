package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A UI component that displays a sun-related data point,
 * along with an icon and a description.
 *
 * @param sunData The data to display.
 * @param icon Resource ID of the icon to display.
 * @param description Description text for the data.
 */
@Composable
fun SunDisplayBox(sunData: String, icon: Int, description: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Display sun icon
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Sun icon",
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Display icon description text
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                // Display the actual sun data
                Text(
                    text = sunData,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}