package no.uio.ifi.in2000.vaeraktiv.ui.location

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AddPlace(defaultPadding: Dp) {
    var expanded by remember { mutableStateOf(false) }
    val rowHeight by animateDpAsState(targetValue = if (expanded) 170.dp else 70.dp)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .height(rowHeight)
            .clickable{expanded = !expanded}
            .clip(RoundedCornerShape(10))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFBCDEFD), Color(0xFF8ACAFB))
                )
            )
    ) {
        if (expanded) AddLocationExpanded(defaultPadding) else AddLocationDefault(defaultPadding)

    }
}