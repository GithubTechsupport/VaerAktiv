package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
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
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
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
            .background(Container, shape = RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "I dag",
            style = MaterialTheme.typography.headlineSmall,
            color = OnContainer,
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

                activities?.get(0) != null -> {
                    val todaysActivities = activities?.get(0)!!.activities
                    AddActivitiesForDay(
                        dayNr = 0,
                        activityDate = ActivityDate("I dag", todaysActivities),
                        isLoading = { uiState.loadingActivities },
                        onRefresh = { dayNr, index -> viewModel.replaceActivityInDay(dayNr, index) },
                        onViewInMap = { activity -> viewModel.viewActivityInMap(activity) },
                        weatherData = mockWeatherData()
                    )
                }
            }
        }
    }
}

private fun mockWeatherData(): List<ForecastForDay> = listOf(
    ForecastForDay("00-06", "15°C", "rain"),
    ForecastForDay("06-12", "18°C", "fog"),
    ForecastForDay("12-18", "20°C", "snow"),
    ForecastForDay("18-24", "16°C", "cloudy")
)

