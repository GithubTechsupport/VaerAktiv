package no.uio.ifi.in2000.vaeraktiv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme{
                Navbar()
           }
        }
    }
}
