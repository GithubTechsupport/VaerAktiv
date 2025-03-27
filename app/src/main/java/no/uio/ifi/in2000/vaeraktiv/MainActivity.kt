package no.uio.ifi.in2000.vaeraktiv

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationDataSource
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var locationDataSource: LocationDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        if (!PermissionManager.isLocationPermissionGranted(this)) {
            PermissionManager.requestLocationPermissions(this)
        } else {
            Log.d("MainActivity", "Location permission already granted")
            collectLastLocation()
        }
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme{
                Navbar()
           }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE) {
            if (PermissionManager.isLocationPermissionGranted(this)) {
                Log.d("MainActivity", "Location permission granted")
                collectLastLocation()
            } else {
                Log.d("MainActivity", "Location permission denied")
            }
        }
    }

    private fun collectLastLocation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Log.d("MainActivity", "collectLastLocation called")
                locationDataSource.getLastLocation().collect { location ->
                    Log.d("MainActivity", location.toString())
                    location?.let {
                        Log.d("MainActivity", "Retrieved location: ${it.latitude}, ${it.longitude}")
                    } ?: Log.d("MainActivity", "Location is null")
                }
            }
        }
    }
}
