package no.uio.ifi.in2000.vaeraktiv.utils

import androidx.lifecycle.LifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationTracker @Inject constructor(
    private val aggregateRepository: IAggregateRepository
) {
    fun start(lifecycleOwner: LifecycleOwner) {
        aggregateRepository.trackDeviceLocation(lifecycleOwner)
    }
}