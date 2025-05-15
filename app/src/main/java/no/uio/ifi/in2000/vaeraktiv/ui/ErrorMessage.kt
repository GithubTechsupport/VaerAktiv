package no.uio.ifi.in2000.vaeraktiv.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Displays an error message in red text with default padding.
 *
 * @param message the error text to show
 * @param modifier optional layout modifiers
 */
@Composable
fun ErrorMessage(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        color = Color.Red,
        modifier = modifier.padding(8.dp)
    )
}
