package no.uio.ifi.in2000.vaeraktiv.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryOnContainer

@Composable
fun SettingsScreen(
    viewModel: PreferencesViewModel
) {
    val preferences by viewModel.userPreferences.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    "Innstillinger",
                    style = MaterialTheme.typography.displaySmall,
                    color = OnContainer
                )
            }
            item {
                SectionHeader("Hvilke aktiviteter liker du?", SecondaryOnContainer)
            }
            items(preferences.take(5)) { preference ->
                Activity(
                    activity = preference,
                    viewModel = viewModel
                )
            }
            item {
                SectionHeader("Sosial eller alene?", SecondaryOnContainer)
            }
            items(preferences.slice(5..6)) { preference ->
                Activity(
                    activity = preference,
                    viewModel = viewModel
                )
            }
            item {
                SectionHeader("Vil du bli tilbudt aktiviteter som koster penger?", SecondaryOnContainer)
            }
            item {
                Activity(
                    activity = preferences[7],
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SectionHeader(text: String, color: Color) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}