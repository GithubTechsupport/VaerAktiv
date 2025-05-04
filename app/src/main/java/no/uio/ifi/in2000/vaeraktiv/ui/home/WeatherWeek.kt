package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.model.ui.getDayOfWeek
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
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
                color = Container,
                shape = RoundedCornerShape(cornerDp)
            )
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Kommende uke",
            style = MaterialTheme.typography.headlineSmall,
            color = OnContainer,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(start = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp)
                .padding(horizontal = 8.dp)
                .height(1.dp)
                .background(OnContainer)
        )

        data.take(7).forEachIndexed { index, day ->
            val dayNr = index + 1
            val isLoading = uiState.loadingFutureActivities.contains(dayNr)
            var expanded by remember { mutableStateOf(false) }
            val activitiesForThisDay = activities?.get(dayNr)
            //val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
            WeatherWeekRow(day, day.icon, context, expanded = expanded, onClick = {
                if (activitiesForThisDay == null && !isLoading) {
                    viewModel.getActivitiesForAFutureDay(dayNr)
                }
                expanded = !expanded
            })
            Log.d("WeatherWeek", "expanded: $expanded")
            AnimatedVisibility(visible = expanded) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ){
                    // Viser værvarsel for dagen
                    DisplayIntervalSymbols(uiState.dayIntervals[index])
                    Spacer(modifier = Modifier.padding(4.dp))
                    DataSection(
                        data = activitiesForThisDay?.takeIf { it.activities.isNotEmpty() },
                        error = if (uiState.isErrorFutureActivities) "Kunne ikke hente aktiviteter" else null,
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
                            onRefresh = { dayNr, indexParam -> viewModel.replaceActivityInDay(dayNr, indexParam) },
                            onViewInMap = { activity -> viewModel.viewActivityInMap(activity) },
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
                    .padding(horizontal = 8.dp)
                    .height(1.dp)
                    .background(OnContainer)
            )
        }
    }
}