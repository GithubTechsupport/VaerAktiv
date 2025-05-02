package no.uio.ifi.in2000.vaeraktiv.utils

import androidx.lifecycle.LifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationTracker @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    fun start(lifecycleOwner: LifecycleOwner) {
        weatherRepository.trackDeviceLocation(lifecycleOwner)
    }
}