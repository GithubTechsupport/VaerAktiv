package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun SunRiseSet(sunData : List<String>) {
    val solOpp = sunData.getOrNull(0) ?: "N/A"
    val solNed = sunData.getOrNull(1) ?: "N/A"
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.inversePrimary,
                shape = RoundedCornerShape(cornerDp))
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "Soloppgang",
                modifier = Modifier
                    .size(80.dp)
                    .padding(12.dp)
            )

            Text(
                text = "Sol opp: $solOpp",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            )

            Box(
                modifier = Modifier
                    .width(1.dp)
                    .padding(top = 4.dp)
                    .height(80.dp)
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            )

            Text(
                text = "Sol ned: $solNed",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.wb_twilight),
                contentDescription = "Solnedgang",
                modifier = Modifier
                    .size(80.dp)
                    .padding(12.dp)
            )

        }
    }
}