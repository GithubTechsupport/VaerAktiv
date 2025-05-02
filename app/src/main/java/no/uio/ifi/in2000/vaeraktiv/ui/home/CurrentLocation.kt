package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

@Composable
fun CurrentLocation(locationName : String, deviceLocation: Location?) { // navn på sted, devicelocation
    var expanded by remember { mutableStateOf(false) }
    var placeName by remember { mutableStateOf(locationName) }
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
            .padding(top = 8.dp)
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.clickable { if (placeName != deviceLocation?.addressName) expanded = !expanded }
        ) {
            Log.d("CurrentLocation", "locationName: $locationName, currentLocation: $deviceLocation")
            Text(
                text = placeName,
                style = MaterialTheme.typography.headlineLarge,
                color = OnContainer,
                textAlign = TextAlign.Center,
                fontSize = 35.sp
            )
            if (expanded) {
                Row (
                    modifier = Modifier.clickable { placeName = deviceLocation?.addressName ?: locationName }
                ) {
                    Text(
                        text = "Din posisjon",
                        fontSize = 20.sp,
                        color = OnContainer
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = "Location",
                        modifier = Modifier.size(20.dp),
                        tint = OnContainer
                    )
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 50.dp)
            .height(1.dp)
            .background(
                color = OnContainer
            )
        )
    }
}