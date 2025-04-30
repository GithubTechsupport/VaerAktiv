package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
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
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

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
                Container,
                shape = RoundedCornerShape(cornerDp))
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(80.dp)
                    .height(160.dp)
                    .background(
                        color = BackGroundColor,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(1.dp, OnContainer, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = solOpp,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.sunset_color),
                        contentDescription = "Sunset icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Sol ned",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }

            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(80.dp)
                    .height(160.dp)
                    .background(
                        color = BackGroundColor,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .border(1.dp, OnContainer, RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = solNed,
                        style = MaterialTheme.typography.labelMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.sunrise1_color),
                        contentDescription = "Sunrise icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Sol opp",
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }
        }
    }
}