package no.uio.ifi.in2000.vaeraktiv.ui.home.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.home.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenUiState
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel

/**
 * Displays the "Today's Activities" section in the home screen.
 *
 * Based on the UI state, it conditionally shows a loading indicator, an error message,
 * or the list of activities for the current day. If no activities are available, a fallback message is shown.
 *
 * @param uiState The UI state of the home screen, used to determine loading/error states.
 * @param activities List of activities retrieved for today.
 * @param viewModel ViewModel used to trigger actions like refreshing or viewing activities on a map.
 */
@Composable
fun TodaysActivitiesSection(
    uiState: HomeScreenUiState,
    activities: List<SuggestedActivities?>?,
    viewModel: HomeScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(MaterialTheme.colorScheme.onBackground, shape = RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        // Section header
        Text(
            text = stringResource(R.string.i_dag),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            when {
                // Show error message if fetching today's activities failed
                uiState.isErrorActivitiesToday -> {
                    ErrorMessage("Error fetching today's activities: ${uiState.errorMessageActivitiesToday}")
                }

                // Show loading shimmer while fetching data
                uiState.isLoadingActivitiesToday -> {
                    LoadAllActivities()
                }

                // Display activities if available
                else -> {
                    activities?.getOrNull(0)?.activities?.let { todaysActivities ->
                        AddActivitiesForDay(
                            dayNr = 0,
                            activityDate = ActivityDate("I dag", todaysActivities),
                            isLoading = { uiState.loadingActivities },
                            onRefresh = { dayNr, index ->
                                viewModel.replaceActivityInDay(dayNr, index)
                            },
                            onViewInMap = { activity ->
                                viewModel.viewActivityInMap(activity)
                            }
                        )
                    } ?: run {
                        // Fallback if no activities are available
                        Text(
                            text = stringResource(R.string.ingen_aktiviteter_for_i_dag),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


