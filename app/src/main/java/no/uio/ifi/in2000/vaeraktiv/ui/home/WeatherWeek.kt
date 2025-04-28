package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainCard
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryCard
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun WeatherWeek(
    data: List<ForecastForDay>,
    viewModel: HomeScreenViewModel,
    uiState: HomeScreenUiState
) {
    val cornerDp = 10.dp
    val context = LocalContext.current
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
              Brush.verticalGradient(
                colors = listOf(MainCard, SecondaryCard)
                ),
                shape = RoundedCornerShape(cornerDp)
            )
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "7-dagersvarsel",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .width(150.dp)
                .padding(top = 4.dp)
                .height(1.dp)
                .background(color = MaterialTheme.colorScheme.onBackground)
        )
        // Titles
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp)
//        ) {
//            Text(
//                text = "Dato",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onBackground,
//                textAlign = TextAlign.Start,
//                modifier = Modifier.weight(1f)
//            )
//            Text(
//                text = "Maks Temp",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onBackground,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.weight(1f)
//            )
//            Text(
//                text = "Dagens Vær",
//                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onBackground,
//                textAlign = TextAlign.End,
//                modifier = Modifier.weight(1f)
//            )
//        }
//        Box(
//            modifier = Modifier
//                .width(200.dp)
//                .padding(top = 4.dp)
//                .height(1.dp)
//                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
//        )
        data.take(7).forEach { day ->
            val date = LocalDate.parse(day.date)
            val activities = uiState.futureActivities[date]
            val isLoading = uiState.loadingFutureActivities.contains(date)
            var expanded by remember { mutableStateOf(false) }
            //val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Log.d("WeatherWeek", "Clicked on ${day.date}, expanded: $expanded")
                            if (activities == null && !isLoading) {
                                Log.d("WeatherWeek", "Fetching activites for ${day.date}")
                                viewModel.getActivitiesForDate(date)
                            }
                            expanded = !expanded
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = getDayOfWeek(day.date),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 20.dp)
                    )
                    //Spacer(modifier = Modifier.weight(0.7f))
                    Text(
                        text = "${day.maxTemp}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 65.dp)
                    )
                    //Spacer(modifier = Modifier.weight(0.7f))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(0.5f)
                    ) {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    day.icon,
                                    "drawable",
                                    context.packageName
                                )
                                    .takeIf { it != 0 } ?: R.drawable.icon_warning_generic_red
                            ),
                            contentDescription = "Vær for ${getDayOfWeek(day.date)}",
                            modifier = Modifier
                                .size(36.dp)
                                .weight(1f, fill = false)
                                .wrapContentWidth(align = Alignment.End)
                        )
                        //Spacer(modifier = Modifier.weight(0.7f))
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (expanded) "Kollaps" else "Ekspander",
                            modifier = Modifier
                                .size(15.dp)
                                .weight(0.5f),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
                AnimatedVisibility(visible = expanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 12.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally)
                            )
                        } else if (uiState.isErrorFutureActivities) {
                            ErrorMessage(message = "Feil ved henting av aktiviteter: ${uiState.errorMessageFutureActivities}")
                        } else if (activities != null && activities.activities.isNotEmpty() ){
                            val activityList = activities.activities.map {
                                Activity (
                                    timeOfDay = "${it.timeStart} - ${it.timeEnd}",
                                    name = it.activity,
                                    desc = it.activityDesc
                                )
                            }
                            AddActivitiesForDay(
                                ActivityDate(
                                    date = getDayOfWeek(day.date),
                                    activeties = activityList
                                )
                            )
                        } else {
                            Text(
                                text = "Ingen aktiviteter tilgjengelig for ${getDayOfWeek(day.date)}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 4.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun getDayOfWeek(date: String): String {
    return try {
        val localDate = LocalDate.parse(date)
        val dayOfWeek = localDate.dayOfWeek
        when (dayOfWeek!!) {
            java.time.DayOfWeek.MONDAY -> "Mandag"
            java.time.DayOfWeek.TUESDAY -> "Tirsdag"
            java.time.DayOfWeek.WEDNESDAY -> "Onsdag"
            java.time.DayOfWeek.THURSDAY -> "Torsdag"
            java.time.DayOfWeek.FRIDAY -> "Fredag"
            java.time.DayOfWeek.SATURDAY -> "Lørdag"
            java.time.DayOfWeek.SUNDAY -> "Søndag"
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        "error"
    }
}