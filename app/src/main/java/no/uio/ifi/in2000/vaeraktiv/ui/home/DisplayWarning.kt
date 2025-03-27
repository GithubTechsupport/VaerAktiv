package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun DisplayWarning(warningInfo : String) {
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))

    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .background(
                Color(0xFFFFD600),
                shape = RoundedCornerShape(cornerDp)
            )
            .clickable { isExpanded = !isExpanded }
            .width(200.dp)
            .padding(4.dp)
            .animateContentSize(), // Animasjon for størrelse
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image (
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "Advarsel ikon",
                modifier = Modifier.size(50.dp)
            )
            Text(
                text ="Advarsel",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = warningInfo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
