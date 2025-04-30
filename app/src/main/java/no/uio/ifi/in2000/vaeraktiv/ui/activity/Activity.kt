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
import no.uio.ifi.in2000.vaeraktiv.ui.theme.BackGroundColor
import no.uio.ifi.in2000.vaeraktiv.ui.theme.Container
import no.uio.ifi.in2000.vaeraktiv.ui.theme.OnContainer

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
            contentColor = if (isActive) Container else BackGroundColor,
            containerColor = if (isActive) OnContainer else BackGroundColor,
            disabledContentColor = BackGroundColor,
            disabledContainerColor = BackGroundColor
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
            style = MaterialTheme.typography.bodyLarge,
            color = if (isActive) BackGroundColor else OnContainer
        )
    }
}