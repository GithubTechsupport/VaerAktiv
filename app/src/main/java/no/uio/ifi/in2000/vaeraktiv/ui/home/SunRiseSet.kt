package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun SunRiseSet(sunData : List<String>) {
    val sunRise = sunData.getOrNull(0) ?: "N/A"
    val sunSet = sunData.getOrNull(1) ?: "N/A"
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(cornerDp))
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(10.dp))
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(cornerDp))
                .border(1.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(cornerDp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SunDisplayBox(sunRise, R.drawable.sunrise_color)
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .width(2.dp)
                    .background(MaterialTheme.colorScheme.onBackground)
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 2.dp)
            )
            SunDisplayBox(sunSet, R.drawable.sunset_color)
        }
    }
}