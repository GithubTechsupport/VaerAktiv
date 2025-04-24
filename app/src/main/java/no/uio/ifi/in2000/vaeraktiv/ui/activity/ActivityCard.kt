package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.ui.Activity

@Composable
fun ActivityCard(
    activity: Activity,
    defaultPadding: Dp
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .wrapContentHeight()
            .clickable{expanded = !expanded}
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.inversePrimary
            )
    ) {
        if (expanded) ActivityCardExpanded(activity) else ActivityCardDefault(activity)
    }
}