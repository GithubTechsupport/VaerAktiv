package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RefreshButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
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