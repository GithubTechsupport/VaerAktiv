package no.uio.ifi.in2000.vaeraktiv.data.location

import android.annotation.SuppressLint
import android.location.Location
import android.os.Handler
import android.os.HandlerThread
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataSource @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {

    // Start receiving continuous location updates as suspendable functions
    @SuppressLint("MissingPermission")
    fun getLocationUpdates(): Flow<Location?> = callbackFlow {
        val thread = HandlerThread("LocationUpdatesThread").apply { start() }
        val handler = Handler(thread.looper)

        val locationRequest = LocationRequest.Builder(102,120_000L).build()
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            callback,
            handler.looper
        )
        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
            thread.quitSafely()
        }
    }.flowOn(Dispatchers.IO)
}
