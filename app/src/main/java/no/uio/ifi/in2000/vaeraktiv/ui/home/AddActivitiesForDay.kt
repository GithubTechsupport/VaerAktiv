package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.home.ActivityDate

@Composable
fun AddActivitiesForDay(
    dayNr: Int,
    activityDate: ActivityDate,
    modifier: Modifier = Modifier,
    isLoading: () -> Set<Pair<Int, Int>>,
    onRefresh: (Int, Int) -> Unit,
    onViewInMap: (ActivitySuggestion) -> Unit
    ) {
    // Column for each day
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        activityDate.activities.forEachIndexed { index, activity ->
            if ((dayNr to index) in isLoading.invoke()) {
                LoadingActivityCard()
            } else {
                ActivityCard(
                    activity = activity,
                    onRefresh = { onRefresh(dayNr, index) },
                    onViewInMap = { onViewInMap(activity) }
                )
            }
        }
    }
}