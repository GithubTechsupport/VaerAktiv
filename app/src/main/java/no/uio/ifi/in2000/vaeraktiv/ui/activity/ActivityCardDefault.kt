package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity

@Composable
fun ActivityCardDefault(
    activity: Activity
) {
    Row {
        Text(
            text = activity.timeOfDay,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = activity.name,
            style = MaterialTheme.typography.bodyLarge

        )
    }
}