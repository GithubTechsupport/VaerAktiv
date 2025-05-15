package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A refresh button with a rotating animation when loading.
 *
 * @param onClick Lambda invoked when the button is clicked.
 * @param isLoading Controls whether the refresh icon should rotate.
 * @param enabled Enables or disables the button click.
 */
@Composable
fun RefreshButton(
    onClick: () -> Unit,
    isLoading: Boolean,
    enabled: Boolean
) {
    val rotation = remember { Animatable(0f) }

    // Rotate icon infinitely when loading; reset rotation when not loading
    LaunchedEffect(isLoading) {
        if (isLoading) {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        } else {
            rotation.snapTo(0f)
        }
    }

    // Refresh icon with clickable modifier, showing rotation animation
    Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = "Refresh button/Get new activity",
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .size(48.dp)
            .clickable(enabled = enabled) { onClick() }
            .padding(4.dp)
    )
}