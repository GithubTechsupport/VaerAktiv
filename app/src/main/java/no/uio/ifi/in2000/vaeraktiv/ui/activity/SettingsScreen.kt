package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryOnContainer
import androidx.compose.material3.HorizontalDivider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: PreferencesViewModel
) {
    val preferences by viewModel.userPreferences.collectAsState()
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showContent = true
    }

    val darkGreen = Color(0xFF2F8166)
    val mediumGreen = Color(0xFF5EC5A3)

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
                    style = TextStyle(
                        fontSize = 40.sp,
                        //fontWeight = FontWeight.Bold,
                        color = darkGreen // White for contrast
                    )
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
                HorizontalDivider(
                    thickness = 1.dp,
                    color = mediumGreen
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
                HorizontalDivider(
                    thickness = 1.dp,
                    color = mediumGreen.copy(alpha = 0.2f)
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
                HorizontalDivider(
                    thickness = 1.dp,
                    color = mediumGreen
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
            color = color, // Medium green for subheadings
            textAlign = TextAlign.Start
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}