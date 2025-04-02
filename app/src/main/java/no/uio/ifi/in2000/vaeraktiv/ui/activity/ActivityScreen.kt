package no.uio.ifi.in2000.vaeraktiv.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityScreen(isOnline: Boolean, viewModel: ActivityViewModel) {
    val uiState by viewModel.activityScreenUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAlertForLocation()
        viewModel.initialize()
    }
    if (uiState.isLoading) {
        Text(text = "Loading...")
    } else if (uiState.isError) {
        Text(text = "Error: ${uiState.errorMessage}")
    } else {
        if (isOnline) {
            if (uiState.activities != null) {
                val activities = uiState.activities!!.activities
                    .groupBy { "${it.dayOfMonth}. ${Month.of(it.month).name.lowercase()}" }
                    .map { (date, activities) ->
                        ActivityDate(
                            date,
                            activities.map {
                                Activity(
                                    "${it.timeStart} - ${it.timeEnd}",
                                    it.activity,
                                    it.activity
                                )
                            }
                        )
                    }


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
        }
    }

}