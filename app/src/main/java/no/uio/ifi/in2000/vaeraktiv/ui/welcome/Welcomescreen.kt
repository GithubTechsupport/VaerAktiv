package no.uio.ifi.in2000.vaeraktiv.ui.welcome

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme

@Composable
fun WelcomeScreen(onStartClick: () -> Unit = {}) {
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        showContent = true
    }

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colors.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = showContent, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WelcomeText()
                Spacer(modifier = Modifier.height(16.dp))
                SlogeText()
                Spacer(modifier = Modifier.height(32.dp))
                MascotWithSpeech()
                Spacer(modifier = Modifier.height(32.dp))
                StartButton(onClick = onStartClick)
            }
        }
    }
}

@Composable
fun WelcomeText() {
    Text(
        text = "Velkommen til VærAktiv",
        style = TextStyle(
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SlogeText() {
    Text(
        text = "Aktivitetstips generert spesifikt for deg og dine interesser",
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
fun MascotWithSpeech() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Hei mitt navn er Simon! Klar for å komme i gang?",
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        MascotSaysHi()
    }
}

@Composable
fun MascotSaysHi() {
    Image(
        painter = painterResource(id = R.drawable.simon_mascot),
        contentDescription = "Maskot",
        modifier = Modifier
            .padding(bottom = 16.dp)
    )
}

@Composable
fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .padding(top = 8.dp)
            .height(56.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Kom i gang",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ShowWelcomeScreenPreview() {
    VaerAktivTheme {
        WelcomeScreen()
    }
}
