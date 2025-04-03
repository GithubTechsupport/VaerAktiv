package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity

@Composable
fun ActivityCardExpanded(
    activity: Activity
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = activity.timeOfDay,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
            )
            Text(
                text = activity.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp)
            )
        }
        Text(
            text = activity.desc,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(10.dp)
        )
    }


}