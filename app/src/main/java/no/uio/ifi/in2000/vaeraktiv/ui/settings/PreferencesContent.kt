package no.uio.ifi.in2000.vaeraktiv.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference

@Composable
fun PreferencesContent(
    preferences: List<Preference>,
    viewModel: PreferencesViewModel,
    modifier: Modifier = Modifier,
    additionalHeaderContent: (@Composable () -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        additionalHeaderContent?.let {
            item { it() }
        }

        item {
            SectionHeader(
                text = stringResource(R.string.hvilke_aktiviteter_liker_du),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(preferences.take(5)) { preference ->
            Activity(activity = preference, viewModel = viewModel)
        }

        item {
            SectionHeader(
                text = stringResource(R.string.sosial_eller_alene),
                color = MaterialTheme.colorScheme.primary
            )
        }
        items(preferences.slice(5..6)) { preference ->
            Activity(activity = preference, viewModel = viewModel)
        }

        item {
            SectionHeader(
                text = stringResource(R.string.vil_du_bli_tilbudt_aktiviteter_som_koster_penger),
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            Activity(activity = preferences[7], viewModel = viewModel)
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