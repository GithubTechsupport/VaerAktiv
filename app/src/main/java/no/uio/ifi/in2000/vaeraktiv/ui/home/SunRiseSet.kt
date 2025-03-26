package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SunRiseSet(sunRise: String, sunSet: String) {
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Sol opp: $sunRise    Sol ned: $sunSet")
    }
}