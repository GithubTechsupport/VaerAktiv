package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun AddActivitiesForDay(
    dayNr: Int,
    activityDate: ActivityDate,
    modifier: Modifier = Modifier,
    isLoading: () -> Set<Pair<Int, Int>>,
    onRefresh: (Int, Int) -> Unit,
    weatherData: List<ForecastForDay>? = null,
    onViewInMap: (ActivitySuggestion) -> Unit
    ) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        if (weatherData != null /*&& !isToday*/) { // legger til værikoner for alle dager utenom dagen i dag
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weatherData.forEach { hours ->
                    val icon = context.resources.getIdentifier(hours.icon, "drawable", context.packageName)
                    Column (
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = hours.date,
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnContainer
                        )
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
            }
        }
        activityDate.activities.forEachIndexed { index, activity ->
            if ((dayNr to index) in isLoading.invoke()) {
                LoadingActivityCard()
            } else {
                ActivityCard(
                    activity = activity,
                    isToday = activityDate.date == "I dag",
                    onRefresh = { onRefresh(dayNr, index) },
                    onViewInMap = { onViewInMap(activity) }
                )
            }
        }
    }
}