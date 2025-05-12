package no.uio.ifi.in2000.vaeraktiv.ui.home

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
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.home.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.ErrorMessage

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
        Text(
            text = "I dag",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            when {
                uiState.isErrorActivitiesToday -> {
                    ErrorMessage("Error fetching today's activities: ${uiState.errorMessageActivitiesToday}")
                }

                uiState.isLoadingActivitiesToday -> {
                    LoadAllActivities()
                }

                else -> {
                    activities?.getOrNull(0)?.activities?.let { todaysActivities ->
                        AddActivitiesForDay(
                            dayNr = 0,
                            activityDate = ActivityDate("I dag", todaysActivities),
                            isLoading = { uiState.loadingActivities },
                            onRefresh = { dayNr, index ->
                                viewModel.replaceActivityInDay(
                                    dayNr,
                                    index
                                )
                            },
                            onViewInMap = { activity -> viewModel.viewActivityInMap(activity) }
                        )
                    } ?: run {
                        Text(
                            text = "Ingen aktiviteter for i dag",
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

