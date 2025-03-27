package no.uio.ifi.in2000.vaeraktiv

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository

import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var locationRepository: LocationRepository

    @RequiresApi(Build.VERSION_CODES.O)
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
        locationRepository.startTracking(this) { location ->
            Log.d("MainActivity", "Last location: $location")
        }
    }
}