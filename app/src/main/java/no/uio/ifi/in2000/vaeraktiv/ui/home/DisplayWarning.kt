package no.uio.ifi.in2000.vaeraktiv.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DisplayWarning(warningInfo : String) {
    Spacer(modifier = Modifier.height(4.dp))

    var isExpanded by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .background(
                Color(0xFFFFD600),
                shape = RoundedCornerShape(25.dp)
            )
            .clickable { isExpanded = !isExpanded }
            .width(150.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Advarsel")

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(warningInfo)
        }
    }
}