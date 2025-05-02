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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryOnContainer
import java.time.LocalDate

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .clickable {
                            if (activitiesForThisDay == null && !isLoading) {
                                viewModel.getActivitiesForAFutureDay(dayNr)
                            }
                            expanded = !expanded
                        }
                ) {
                    Text(
                        text = getDayOfWeek(day.date),
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnContainer,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .width(80.dp)// Tar tilgjengelig plass til venstre
                    )
                    Spacer(modifier = Modifier.weight(0.8f))
                    Text(
                        text = "${day.maxTemp}°",
                        style = MaterialTheme.typography.bodyLarge,
                        color = SecondaryOnContainer,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .wrapContentWidth() // Tar kun nødvendig bredde for å være sentrert
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Fyller tomrommet til høyre
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.wrapContentWidth().weight(1f)
                    ) {
                        Image(
                            painter = painterResource(
                                id = context.resources.getIdentifier(
                                    day.icon,
                                    "drawable",
                                    context.packageName
                                ).takeIf { it != 0 } ?: R.drawable.icon_warning_extreme),
                            contentDescription = "Dagens vær",
                            modifier = Modifier
                                .size(40.dp)
                        )
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand/Collapse",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(12.dp)
                        )
                    }
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ){
                    // Viser værvarsel for dagen
                    DisplayIntervalSymbols(uiState.dayIntervals[index])
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isLoading) {
                        LoadAllActivities()
                    } else if (uiState.isErrorFutureActivities) {
                        ErrorMessage("Faen")
                    } else if (activitiesForThisDay != null && activitiesForThisDay.activities.isNotEmpty()){
                        val activitiesList = activitiesForThisDay.activities
                        AddActivitiesForDay(
                            dayNr = dayNr,
                            activityDate = ActivityDate (
                                date = getDayOfWeek(day.date),
                                activities = activitiesList,
                            ),
                            isLoading = { uiState.loadingActivities },
                            onRefresh = { dayNr, indexParam -> viewModel.replaceActivityInDay(dayNr, indexParam) },
                            onViewInMap = { activity -> viewModel.viewActivityInMap(activity) },
                        )
                    } else {
                        Text(
                            text = "Finner ingen aktiviteter",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnContainer,
                            modifier = Modifier.padding(start = 8.dp)
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