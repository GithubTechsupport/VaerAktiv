package no.uio.ifi.in2000.EmptyApplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.EmptyApplication.data.alerts.MetAlertsDataSource
import no.uio.ifi.in2000.EmptyApplication.data.alerts.MetAlertsRepository
import no.uio.ifi.in2000.EmptyApplication.ui.theme.EmptyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmptyApplicationTheme {
            }
        }
    }
}

