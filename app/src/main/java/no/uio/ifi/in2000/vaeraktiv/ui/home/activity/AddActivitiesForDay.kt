package no.uio.ifi.in2000.vaeraktiv.ui.home.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.home.ActivityDate

/**
 * Displays a list of activities for a given day.
 *
 * @param dayNr The index of the day.
 * @param activityDate Object containing activities for the day.
 * @param modifier Layout modifier.
 * @param isLoading Returns a set of pairs indicating which activities are loading.
 * @param onRefresh Called when the user wants to refresh an activity.
 * @param onViewInMap Called when the user clicks "view in map" for an activity.
 */
@Composable
fun AddActivitiesForDay(
    dayNr: Int,
    activityDate: ActivityDate,
    modifier: Modifier = Modifier,
    isLoading: () -> Set<Pair<Int, Int>>,
    onRefresh: (Int, Int) -> Unit,
    onViewInMap: (ActivitySuggestion) -> Unit
) {
    // Column for today's activities
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        activityDate.activities.forEachIndexed { index, activity ->
            // Show loading screen if the activity is being updated
            if ((dayNr to index) in isLoading.invoke()) {
                LoadingActivityCard()
            } else {
                // Show activity card
                ActivityCard(
                    activity = activity,
                    onRefresh = { onRefresh(dayNr, index) },
                    onViewInMap = { onViewInMap(activity) }
                )
            }
        }
    }
}