package no.uio.ifi.in2000.vaeraktiv.ui.home.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.home.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.DataSection
import no.uio.ifi.in2000.vaeraktiv.ui.home.DisplayIntervalSymbols
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenUiState
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.home.activity.AddActivitiesForDay
import no.uio.ifi.in2000.vaeraktiv.ui.home.activity.LoadAllActivities


/**
 * Displays the weather and activity forecast for the upcoming week.
 *
 * For each of the next 7 days, it shows a row with a summary of weather data,
 * and when expanded, details including interval symbols and suggested activities.
 *
 * Activities are fetched on-demand when a day is expanded and no cached data exists.
 *
 * @param activities A list of [SuggestedActivities] for each day of the week (nullable for missing data).
 * @param viewModel The [no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel] used to fetch and update activity data.
 * @param uiState A [no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenUiState] holding the UI's current state, including weather and loading flags.
 */

@Composable
fun WeatherWeek(
    activities: List<SuggestedActivities?>?,
    viewModel: HomeScreenViewModel,
    uiState: HomeScreenUiState
) {
    val cornerDp = 10.dp
    val context = LocalContext.current
    val data = uiState.thisWeeksWeather

    Spacer(modifier = Modifier.height(12.dp))

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(cornerDp)
            )
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        // Section title
        Text(
            text = stringResource(R.string.kommende_uke),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 8.dp)
        )

        // Divider
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
                .padding(horizontal = 8.dp)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground)
        )

        // Iterate over the first 7 days of weather data
        data.take(7).forEachIndexed { index, day ->
            val dayNr = index + 1
            val isLoading = uiState.loadingFutureActivities.contains(dayNr)
            var expanded by remember { mutableStateOf(false) }
            val activitiesForThisDay = activities?.get(dayNr)
            WeatherWeekRow(day, day.icon, context, expanded = expanded, onClick = {
                if (activitiesForThisDay == null && !isLoading) {
                    viewModel.getActivitiesForAFutureDay(dayNr)
                }
                expanded = !expanded
            })

            // Expandable section for showing details
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    // Weather symbols for different times of day
                    DisplayIntervalSymbols(uiState.dayIntervals[index])
                    Spacer(modifier = Modifier.padding(4.dp))

                    // Display activities, loading, or error based on state
                    DataSection(
                        data = activitiesForThisDay?.takeIf { it.activities.isNotEmpty() },
                        error = if (uiState.isErrorFutureActivities)
                            stringResource(R.string.kunne_ikke_hente_aktiviteter)
                        else null,
                        loading = isLoading,
                        errorMessagePrefix = "Feil:",
                        loadingContent = { LoadAllActivities() }
                    ) { activityDate ->
                        AddActivitiesForDay(
                            dayNr = dayNr,
                            activityDate = ActivityDate(
                                date = getDayOfWeek(day.date),
                                activities = activityDate.activities
                            ),
                            isLoading = { uiState.loadingActivities },
                            onRefresh = { dayNr, indexParam ->
                                viewModel.replaceActivityInDay(dayNr, indexParam)
                            },
                            onViewInMap = { activity ->
                                viewModel.viewActivityInMap(activity)
                            },
                        )
                    }
                }
            }

            // Divider between days
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .padding(horizontal = 8.dp)
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}