package no.uio.ifi.in2000.vaeraktiv.ui.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.ui.settings.Activity
import no.uio.ifi.in2000.vaeraktiv.ui.settings.PreferencesViewModel


@Composable
fun InfoPeferencesScreen(
    viewModel: PreferencesViewModel,
    onContinueClick: () -> Unit
) {
    val preferences by viewModel.userPreferences.collectAsState()
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showContent = true
    }

    val darkGreen = Color(0xFF2F8166)
    val mediumGreen = Color(0xFF5EC5A3)
    val lightCyan = Color(0xFFE0F7FA)
    val red = Color(0xFFA1283C)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(lightCyan, mediumGreen)
                )
            )
    ) {
        AnimatedVisibility(visible = showContent, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        MascotIntroSpeech()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        SectionHeader("Hvilke aktiviteter liker du?", red)
                    }
                    items(preferences.take(5)) { preference ->
                        Activity(
                            activity = preference,
                            viewModel = viewModel
                        )
                        Divider(
                            color = mediumGreen.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                    item {
                        SectionHeader("Sosial eller alene?", red)
                    }
                    items(preferences.slice(5..6)) { preference ->
                        Activity(
                            activity = preference,
                            viewModel = viewModel
                        )
                        Divider(
                            color = mediumGreen.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                    item {
                        SectionHeader("Vil du bli tilbudt aktiviteter som koster penger?", red)
                    }
                    item {
                        Activity(
                            activity = preferences[7],
                            viewModel = viewModel
                        )
                        Divider(
                            color = mediumGreen.copy(alpha = 0.2f),
                            thickness = 1.dp
                        )
                    }
                }
                ContinueButton(onClick = onContinueClick)
            }
        }
    }
}

@Composable
fun MascotIntroSpeech() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Før vi setter i gang, vil jeg gjerne vite hva du foretrekker og hva du liker å gjøre når du er fysisk aktiv.",
            color = Color(0xFF2F8166), // darkGreen
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.simon_mascot),
            contentDescription = "Maskot",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(100.dp)
        )
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

@Composable
fun ContinueButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F8166)),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Fortsett",
            fontSize = 16.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = Color.White
        )
    }
}