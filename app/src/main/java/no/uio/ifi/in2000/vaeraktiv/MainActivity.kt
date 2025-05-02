package no.uio.ifi.in2000.vaeraktiv

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.vaeraktiv.ui.activity.PreferencesViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.map.MapScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.KeepAliveManager
import no.uio.ifi.in2000.vaeraktiv.utils.LocationTracker
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val homeScreenViewModel: HomeScreenViewModel by viewModels()
    private val favoriteLocationViewModel: FavoriteLocationViewModel by viewModels()
    private val mapScreenViewModel: MapScreenViewModel by viewModels()
    private val preferencesViewModel: PreferencesViewModel by viewModels()
    
    @Inject
    lateinit var locationTracker: LocationTracker
    

    @RequiresApi(Build.VERSION_CODES.O)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KeepAliveManager().apply {
            start()
            lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    stop()
                }
            })
        }
        Log.d("MainActivity", "onCreate called")
        checkPermissions(locationTracker)
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme {
                Navbar(favoriteLocationViewModel, homeScreenViewModel, mapScreenViewModel, preferencesViewModel)
            }
        }
    }

    public fun checkPermissions(locationTracker: LocationTracker) {
        if (!PermissionManager.isLocationPermissionGranted(this)) {
            PermissionManager.requestLocationPermissions(this)
        } else {
            Log.d("MainActivity", "Location permission already granted")
            locationTracker.start(this)
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
                locationTracker.start(this)
            } else {
                Log.d("MainActivity", "Location permission denied")
            }
        }
    }
}