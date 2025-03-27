package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate

@Composable
fun AddActivitiesForDay(
    activity: ActivityDate
    ) {
    Column {
        Text(
            text = activity.date,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
        activity.activeties.forEach {
            ActivityCard(it, 10.dp)
        }
    }
}