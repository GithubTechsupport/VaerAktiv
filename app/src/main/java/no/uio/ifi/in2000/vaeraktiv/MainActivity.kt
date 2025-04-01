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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.ui.location.FavoriteLocationViewModel

import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var locationRepository: LocationRepository
    @Inject lateinit var geocoderClass: GeocoderClass
    private val favoriteLocationViewModel: FavoriteLocationViewModel by viewModels()
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
                Navbar(favoriteLocationViewModel)
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
            //val coordinates = Pair(location.latitude, location.longitude)
            val coordinates = Pair(59.9522, 10.8874)
            Log.d("MainActivity", "Coordinates: $coordinates")
            CoroutineScope(Dispatchers.Main).launch {
            try {
                val address = geocoderClass.getLocationFromCoordinates(coordinates)
                if (address != null) {
                    Log.d("MainActivity", address.toString())
                } else {
                    Log.d("MainActivity", "Address is unknown")
                }
            } catch (e: Exception) {
                Log.d("MainActivity", "Error getting location name: ${e.message}")
            }
            }
        }
    }
}