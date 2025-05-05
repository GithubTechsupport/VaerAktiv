//package no.uio.ifi.in2000.vaeraktiv.ui.home
//
//import android.annotation.SuppressLint
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.KeyboardArrowDown
//import androidx.compose.material.icons.filled.KeyboardArrowUp
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import no.uio.ifi.in2000.vaeraktiv.model.ai.JsonResponse
//import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
//import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
//import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
//
//@RequiresApi(Build.VERSION_CODES.O)
//@SuppressLint("DiscouragedApi")
//@Composable
//fun WeatherDayRow(
//    dayName: String,
//    day: ForecastForDay,
//    activities: JsonResponse?,
//    isLoading: Boolean,
//    isError: Boolean,
//    errorMessage: String,
//    onClick: () -> Unit,
//    context: android.content.Context
//) {
//    var expanded by remember { mutableStateOf(false) }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 4.dp)
//            .clickable {
//                if (!expanded && activities == null && !isLoading) {
//                    onClick()
//                }
//                expanded = !expanded
//            },
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//            contentColor = MaterialTheme.colorScheme.onSurface
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column (
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = dayName,
//                    style = MaterialTheme.typography.titleMedium,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                Text(
//                    text = day.maxTemp + "°",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
//                val iconResId = context.resources.getIdentifier(day.icon, "drawable", context.packageName)
//                Image(
//                    painter = painterResource(id = iconResId),
//                    contentDescription = null,
//                    modifier = Modifier.size(48.dp)
//                )
//                Icon(
//                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                    contentDescription = if (expanded) "Kollaps" else "Ekspander",
//                    modifier = Modifier.size(24.dp),
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//            AnimatedVisibility(visible = expanded) {
//                Column(
//                    modifier = Modifier.padding(top = 8.dp)
//                ) {
//                    if (isLoading) {
//                        CircularProgressIndicator(
//                            modifier = Modifier
//                                .size(24.dp)
//                                .align(Alignment.CenterHorizontally),
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    } else if (isError) {
//                        ErrorMessage(message = "Feil ved henting av aktiviteter: $errorMessage")
//                    } else if (activities != null && activities.activities.isNotEmpty()) {
//                        val activityList = activities.activities.map {
//                            Activity(
//                                timeOfDay = "${it.timeStart} - ${it.timeEnd}",
//                                name = it.activity,
//                                desc = it.activityDesc
//                            )
//                        }
//                        AddActivitiesForDay(ActivityDate(date = day.date, activityList))
//                    } else {
//                        Text(
//                            text = "Ingen aktiviteter tilgjengelig for $dayName",
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            modifier = Modifier.padding(8.dp)
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
fun printerhei(){
    print("HEI")
}