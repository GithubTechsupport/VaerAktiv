package no.uio.ifi.in2000.vaeraktiv

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.vaeraktiv.ui.activity.ActivityViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val favoriteLocationViewModel: FavoriteLocationViewModel by viewModels()
    private val activityViewModel: ActivityViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        if (!PermissionManager.isLocationPermissionGranted(this)) {
            PermissionManager.requestLocationPermissions(this)
        } else {
            Log.d("MainActivity", "Location permission already granted")
            activityViewModel.startTracking(this)
        }
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme{
                Navbar(favoriteLocationViewModel, activityViewModel)
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
                activityViewModel.startTracking(this)
            } else {
                Log.d("MainActivity", "Location permission denied")
            }
        }
    }
}