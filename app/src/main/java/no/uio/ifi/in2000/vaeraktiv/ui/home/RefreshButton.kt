package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun RefreshButton(onClick: () -> Unit, isLoading: Boolean) {
    val rotation = remember { Animatable(0f) }
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
    Image(
        painter = painterResource(id = R.drawable.refresh),
        contentDescription = "Refresh button",
        modifier = Modifier
            .size(30.dp)
            .rotate(rotation.value)
            .clickable(enabled = !isLoading) { onClick() }
            .padding(4.dp)
    )
}