package no.uio.ifi.in2000.vaeraktiv.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryOnContainer
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: PreferencesViewModel, navController: NavController
) {
    val preferences by viewModel.userPreferences.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {
        IconButton(
            onClick = { navController.popBackStack("home", false) },
            modifier = Modifier.size(48.dp).align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = OnContainer,
                modifier = Modifier
                    .padding(top = 8.dp).padding(start = 8.dp)
                    .size(35.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
            }
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