package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion

/**
 * Displays a card with an activity suggestion.
 *
 * @param activity The activity being displayed.
 * @param onRefresh Called when the refresh button is pressed.
 * @param onViewInMap Called when "View in map" is pressed.
 */
@Composable
fun ActivityCard(
    activity: ActivitySuggestion,
    onRefresh: (() -> Unit),
    onViewInMap: (() -> Unit)
) {

    // Card that shows the content
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // Header with the activity's name and refresh button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.activityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                RefreshButton(
                    onClick = { onRefresh.invoke() },
                    isLoading = false,
                    enabled = true
                )
            }

            // Time period for the activity
            Row {
                Text(
                    text = stringResource(
                        R.string.beste_tidspunkt,
                        activity.timeStart,
                        activity.timeEnd
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Activity description
            Row {
                Text(
                    text = activity.activityDesc,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Link to map view (if not a custom activity)
            Row(
                modifier = Modifier.align(Alignment.End)
            ) {
                if (activity !is CustomActivitySuggestion) {
                    Text(
                        text = stringResource(R.string.vis_i_kart),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable { onViewInMap.invoke() }
                    )
                }
            }
        }
    }
}