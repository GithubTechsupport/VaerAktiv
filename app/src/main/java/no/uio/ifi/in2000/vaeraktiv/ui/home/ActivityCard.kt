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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer
import no.uio.ifi.in2000.vaeraktiv.ui.theme.PrimaryNavbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryOnContainer
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.text.style.TextDecoration
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion


@Composable
fun ActivityCard(
    activity: ActivitySuggestion,
    isToday: Boolean,
    onRefresh: (() -> Unit),
    onViewInMap: (() -> Unit)
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(BackGroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
        ) {
            // Tidsrom og navn på samme linje
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.activityName,
                    style = MaterialTheme.typography.titleLarge,
                    color = OnContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                RefreshButton(
                    onClick = {
                        onRefresh.invoke()
                              },
                    isLoading = false,
                    enabled = true
                )
            }
            Row {
                Text(
                    text = "Beste tidspunkt: ${activity.timeStart} - ${activity.timeEnd}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row {
                // Beskrivelse under
                Text(
                    text = activity.activityDesc,
                    style = MaterialTheme.typography.bodyLarge,
                    color = SecondaryOnContainer,
                    modifier = Modifier.padding(top = 8.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row (
                modifier = Modifier.align(Alignment.End)
            ){
                if (activity !is CustomActivitySuggestion) {
                    Text(
                        text = "Vis i kart",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnContainer,
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