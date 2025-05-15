package no.uio.ifi.in2000.vaeraktiv.ui.preferences

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference

/**
 * Renders a toggleable button for a single user preference.
 *
 * @param activity the Preference item to display and toggle
 * @param viewModel ViewModel handling preference state changes
 */
@Composable
fun Activity(
    activity: Preference,
    viewModel: PreferencesViewModel
) {

    Button(
        onClick = {
            viewModel.onPreferenceToggled(activity, !activity.isEnabled)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (activity.isEnabled) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background,
            contentColor = if (activity.isEnabled) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
        ),
        border = if(!activity.isEnabled) BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground) else null,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        if (activity.isEnabled) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = activity.desc,
            style = MaterialTheme.typography.bodyLarge,
            color = if (activity.isEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )
    }
}
