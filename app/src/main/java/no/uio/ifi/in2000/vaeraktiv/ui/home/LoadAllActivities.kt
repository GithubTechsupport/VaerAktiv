package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadAllActivities(modifier: Modifier = Modifier) {
    // Displays a vertical list of placeholder loading cards for activities
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        LoadingActivityCard()
        LoadingActivityCard()
        LoadingActivityCard()
    }
}
