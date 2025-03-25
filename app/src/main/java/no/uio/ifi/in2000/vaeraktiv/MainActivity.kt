package no.uio.ifi.in2000.vaeraktiv

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationDataSource
import no.uio.ifi.in2000.vaeraktiv.ui.navbar.Navbar
import no.uio.ifi.in2000.vaeraktiv.ui.theme.VaerAktivTheme
import no.uio.ifi.in2000.vaeraktiv.utils.PermissionManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //@Inject lateinit var locationDataSource: LocationDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MainActivity", "onCreate called")
        super.onCreate(savedInstanceState)
//        if (!PermissionManager.isLocationPermissionGranted(this)) {
//            PermissionManager.requestLocationPermissions(this)
//        } else {
//            Log.d("MainActivity", "Location permission already granted")
//            //Log.d("MainActivity", locationDataSource.getLastLocation().toString())
//        }
        enableEdgeToEdge()
        setContent {
            VaerAktivTheme{
                Text(text = "VaerAktiv")
           }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PermissionManager.LOCATION_PERMISSION_REQUEST_CODE) {
//            if (PermissionManager.isLocationPermissionGranted(this)) {
//                Log.d("MainActivity", "Location permission granted")
//                //Log.d("MainActivity", locationDataSource.getLastLocation().toString())
//            } else {
//                Log.d("MainActivity", "Location permission denied")
//            }
//        }
//    }
}
