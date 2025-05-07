package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/*
* This function NoNetworkDialog is a dialog that will be displayed when the device is offline.
* This dialog has information about that the device is offline and has two buttons, one to retry and one to close the app.
*
* */
@Composable
fun NoNetworkDialog(onRetry: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Ingen Internettforbindelse") },
        text = { Text("Du må være tilkoblet internett for å laste inn data.") },
        confirmButton = {
            Button(
                onClick = { onRetry() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )) {
                Text("Prøv igjen")
            }
        },
        dismissButton = {
            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )) {
                Text("Avslutt")
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}