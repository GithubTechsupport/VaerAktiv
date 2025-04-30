package no.uio.ifi.in2000.vaeraktiv.ui.location

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
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container

@Composable
fun AddPlace(defaultPadding: Dp, viewModel: FavoriteLocationViewModel) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .wrapContentHeight()
            .clickable{expanded = !expanded}
            .clip(RoundedCornerShape(10))
            .background(Container)
    ) {
        if (expanded) AddLocationExpanded(defaultPadding, viewModel) else AddLocationDefault(defaultPadding)

    }
}