package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.FavoriteLocation

@SuppressLint("DiscouragedApi")
@Composable
fun PlaceCard(
    location: FavoriteLocation,
    defaultPadding: Dp
) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(
        location.iconDesc,
        "drawable",
        context.packageName
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBCDEFD), Color(0xFF8ACAFB))
                )
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    horizontal = 20.dp,
                    vertical = defaultPadding)
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(defaultPadding)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.FavoriteBorder,
                contentDescription = "Heart Icon",
                modifier = Modifier
                    .padding(defaultPadding)
                    .size(30.dp)
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding)
            .height(1.dp)
            .background(color = MaterialTheme.colorScheme.onBackground)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = location.iconDesc,
                    modifier = Modifier
                        .padding(defaultPadding)
                )
            }
            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = location.shortDesc,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = "${location.uv} UV",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = "${location.highestTemp}°/${location.lowestTemp}°",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${location.wind} m/s",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = "nedbør: ${location.downPour} ml",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                }
            }
        }
    }
}