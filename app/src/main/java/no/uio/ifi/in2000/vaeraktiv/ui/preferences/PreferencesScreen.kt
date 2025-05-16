package no.uio.ifi.in2000.vaeraktiv.ui.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

/**
 * Screen for adjusting user preferences with a back navigation action.
 *
 * @param viewModel ViewModel coordinating preference data and navigation events
 */
@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel,
) {
    val preferences by viewModel.userPreferences.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(48.dp)
                .clickable(
                    enabled = true,
                    onClick = { viewModel.navigateBack() }
                )
        )
        PreferencesContent(
            preferences = preferences,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 35.dp, bottom = 2.dp),
            additionalHeaderContent = {
                Text(
                    text = stringResource(R.string.innstillinger),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        )
    }
}
