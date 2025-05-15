package no.uio.ifi.in2000.vaeraktiv.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.core.app.ActivityCompat

/**
 * Helper for requesting and checking location permissions at runtime.
 */
class PermissionManager {
    companion object {
        /**
         * Returns true if either fine or coarse location permission is granted.
         */
        fun isLocationPermissionGranted(activity: Activity): Boolean {
            return ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        /**
         * Call this in your Activity to get a launcher you can use to request permissions.
         * onResult(true) means at least one location permission was granted.
         */
        fun registerForLocationPermissions(
            activity: ComponentActivity,
            onResult: (Boolean) -> Unit
        ): ActivityResultLauncher<Array<String>> {
            return activity.registerForActivityResult(RequestMultiplePermissions()) { perms ->
                val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                onResult(granted)
            }
        }
    }
}
