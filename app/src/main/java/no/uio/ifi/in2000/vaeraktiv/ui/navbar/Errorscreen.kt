package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/*
* Message dialog for errors.
* to-do if we want to send with the error message in det vindow
* */
@Composable
fun ErrorScreen(onDismissRequest: () -> Unit, message: String = "Det oppsto en feil!") {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { androidx.compose.material3.Text("Feil!") },
        text = { androidx.compose.material3.Text(message) },
        confirmButton = {
            Button(onClick = { onDismissRequest() }) {
                androidx.compose.material3.Text("OK")
            }
        },
        modifier = Modifier
    )
}
@Preview(showBackground = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(onDismissRequest = {})
}