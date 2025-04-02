package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunProperties

@Composable
fun SunRiseSet(sunData : SunProperties) {
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                color = Color(0xFFBCDEFD),
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
                text = "Sol opp: ${sunData.sunrise.time}",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            )

            Text(
                text = "Sol ned: ${sunData.sunset.time}",
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