package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TodaysWeather(hours: List<String>, temps: List<String>, rain: List<String>) {
    Spacer(modifier = Modifier.height(4.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color.LightGray)
    ) {
        Text("Været i dag")
//        LazyRow(
//            modifier = Modifier.padding(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(hours.size) { index ->
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .width(80.dp)
//                ) {
//                    Text(
//                        text = hours[index],
//                    )
//                    Text(
//                        text = "${temps[index]}°C",
//                    )
//                    Text(
//                        text = "${rain[index]} mm",
//                    )
//                }
//            }
//        }
    }
}