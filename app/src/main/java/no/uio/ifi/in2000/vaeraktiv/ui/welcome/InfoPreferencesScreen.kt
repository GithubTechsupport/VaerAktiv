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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.ui.preferences.PreferencesContent
import no.uio.ifi.in2000.vaeraktiv.ui.preferences.PreferencesViewModel

/*

 * This is the second screen in the welcome flow.
 * Here, the user is asked to select their preferences so that Simon can generate activities based on them.
 * After this screen, the user continues to the third and final welcome screen.
 *
 * This class uses a shared layout called PreferenceContent.kt, which is reused in two different places.
 * Reusing that component makes the design more intuitive and consistent.
 * In this welcome version, we add a custom title and a navigation button that are not shown in the regular preferences settings.
*/


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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        AnimatedVisibility(visible = showContent, enter = fadeIn()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PreferencesContent(
                    preferences = preferences,
                    viewModel = viewModel,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    additionalHeaderContent = { MascotIntroSpeech() }
                )
                ContinueButton(onClick = onContinueClick)
            }
        }
    }
}

@Composable
fun MascotIntroSpeech() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = stringResource(R.string.f_r_vi_setter_i_gang_vil_jeg_gjerne_vite_hva_du_foretrekker_og_hva_du_liker_gj_re_n_r_du_er_fysisk_aktiv),
            color = MaterialTheme.colorScheme.primary,
            fontStyle = FontStyle.Italic,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.simon_mascot),
            contentDescription = "Mascot",
            modifier = Modifier
                .padding(bottom = 16.dp)
                .size(100.dp)
        )
    }
}

@Composable
fun ContinueButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.fortsett),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
