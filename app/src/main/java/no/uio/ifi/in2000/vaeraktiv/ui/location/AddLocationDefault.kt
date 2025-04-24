package no.uio.ifi.in2000.vaeraktiv.ui.location

import android.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AddLocationDefault(defaultPadding: Dp) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Text(
            text = "Legg til sted",
            style = MaterialTheme.typography.titleMedium ,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Left,
            modifier = Modifier
                .padding(defaultPadding)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.AddCircle,
            contentDescription = "Add Circle Icon",
            modifier = Modifier
                .padding(defaultPadding)
                .size(50.dp)
        )
    }
}