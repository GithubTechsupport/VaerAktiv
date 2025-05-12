package no.uio.ifi.in2000.vaeraktiv

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.map.MapScreenViewModel
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.settings.PreferencesViewModel
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("MainActivity", "Location permission granted")
            locationTracker.start(this)
        } else {
            Log.d("MainActivity", "Location permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
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
        checkPermissions()
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme {
                val navController: NavHostController = rememberNavController()
                Navbar(
                    favoriteLocationViewModel,
                    homeScreenViewModel,
                    mapScreenViewModel,
                    preferencesViewModel,
                    navController
                )
            }
        }
    }

    private fun checkPermissions() {
        if (!PermissionManager.isLocationPermissionGranted(this)) {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            Log.d("MainActivity", "Location permission already granted")
            locationTracker.start(this)
        }
    }
}