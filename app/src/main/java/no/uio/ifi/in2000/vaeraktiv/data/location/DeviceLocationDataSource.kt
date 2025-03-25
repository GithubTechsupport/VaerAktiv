package no.uio.ifi.in2000.vaeraktiv.data.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSource @Inject constructor(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    // Get last known location as a suspendable function
    @SuppressLint("MissingPermission")
    fun getLastLocation(): Flow<Location?> = callbackFlow {
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                trySend(location)
                close()
            }
            .addOnFailureListener {
                close(it)
            }
        awaitClose {}
    }

    // Start receiving continuous location updates as a suspendable functions
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location> = callbackFlow {
        val locationRequest = LocationRequest.Builder(102,10_1000L).build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )
        awaitClose { fusedLocationProviderClient.removeLocationUpdates(callback) }
    }
}
