package no.uio.ifi.in2000.vaeraktiv.utils

import androidx.lifecycle.LifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Tracks device location updates by delegating to the repository.
 */
@Singleton
class LocationTracker @Inject constructor(
    private val aggregateRepository: IAggregateRepository
) {
    /**
     * Begins observing location updates tied to the given lifecycle owner.
     */
    fun start(lifecycleOwner: LifecycleOwner) {
        aggregateRepository.trackDeviceLocation(lifecycleOwner)
    }
}
