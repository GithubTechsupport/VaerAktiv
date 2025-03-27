package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate

@Preview(showBackground = true)
@Composable
fun ActivityScreen() {
    val activitiesDate = ActivityDate("27. mars", listOf(Activity("10-12", "Gå tur", "Nå er det fint vær, en tur langs akerselva hadde vært fint i dag")))
    val activities: List<ActivityDate> = listOf(activitiesDate)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF6BAEDF), Color(0xFF8ACAFF))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item {
            activities.forEach {
                AddActivitiesForDay(it)
            }
        }
    }
}