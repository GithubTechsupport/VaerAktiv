package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate

@Composable
fun AddActivitiesForDay(
    activityDate: ActivityDate,
    modifier: Modifier = Modifier,
    onRefresh: ((Int) -> Unit)? = null
    ) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.Start
    ){
        activityDate.activeties.forEachIndexed { index, activity ->
            ActivityCard(
                activity = activity,
                isToday = activityDate.date == "I dag",
                onRefresh = if (activityDate.date == "I dag") {{ onRefresh?.invoke(index) }} else null
            )
        }
    }
}