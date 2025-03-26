package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DisplayWeather(temp : String, uv: String, rain: String, wind: String) {
    Spacer(modifier = Modifier.height(4.dp))
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row { Text("$temp°C") }
        Row { Text("$uv UV   $rain   $wind") }
    }
}