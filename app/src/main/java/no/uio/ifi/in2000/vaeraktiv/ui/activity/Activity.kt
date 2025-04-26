package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference

@Composable
fun Activity(
    activity : Preference
    ) {
    //TODO: Sette inn en viewmodel som håndterer aktivitetene
    var isActive by rememberSaveable { mutableStateOf(activity.isEnabled) }
    OutlinedButton(
        onClick = {
            isActive = !isActive
        },
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = if (isActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            containerColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        Text(
            text = activity.desc,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}