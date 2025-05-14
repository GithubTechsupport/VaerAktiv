package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R


/*
* This function NoNetworkDialog is a dialog that will be displayed when the device is offline.
* This dialog has information about that the device is offline and has two buttons, one to retry and one to close the app.
*
* */
@Composable
fun NoNetworkDialog(onRetry: () -> Unit, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.ingen_internettforbindelse)) },
        text = { Text(stringResource(R.string.du_m_v_re_tilkoblet_internett_for_laste_inn_data)) },
        confirmButton = {
            Button(
                onClick = { onRetry() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )) {
                Text(stringResource(R.string.pr_v_igjen))
            }
        },
        dismissButton = {
            Button(
                onClick = { onClose() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )) {
                Text(stringResource(R.string.avslutt))
            }
        },
        modifier = Modifier.padding(16.dp)
    )
}