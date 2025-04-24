package no.uio.ifi.in2000.vaeraktiv.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity
import no.uio.ifi.in2000.vaeraktiv.model.ui.ActivityDate
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.LoadingScreen
import java.time.Month

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActivityScreen(isOnline: Boolean, viewModel: ActivityScreenViewModel) {
    val uiState by viewModel.activityScreenUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }
    if (uiState.isLoading) {
        LoadingScreen()
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
                                    it.activityDesc,
                                )
                            }
                        )
                    }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.primaryContainer),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    item{
                        OutlinedButton(
                            onClick = { viewModel.getActivities() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Black
                            )

                        ) {
                            Text(
                                text = "Finn nye aktiviteter",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
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