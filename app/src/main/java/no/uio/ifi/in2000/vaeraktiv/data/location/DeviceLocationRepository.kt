package no.uio.ifi.in2000.vaeraktiv.data.location

import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationDataSource: LocationDataSource
) {

    private var trackingJob: Job? = null

    fun startTracking(
        lifecycleOwner: LifecycleOwner,
        onLocationUpdate: (Location) -> Unit
    ) {
        trackingJob?.cancel()
        trackingJob = lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationDataSource.getLocationUpdates().collect { location ->
                    location?.let { onLocationUpdate(it) }
                }
            }
        }
    }
}