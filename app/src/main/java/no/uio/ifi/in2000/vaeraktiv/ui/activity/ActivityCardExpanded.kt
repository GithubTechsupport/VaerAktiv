package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity

@Composable
fun ActivityCardExpanded(
    activity: Activity
) {
    Row {
        Text(
            text = activity.timeOfDay
        )
        Text(
            text = activity.name
        )
    }
}