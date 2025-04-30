package no.uio.ifi.in2000.vaeraktiv.utils

import androidx.lifecycle.LifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import javax.inject.Inject

class LocationTracker @Inject constructor(private val weatherRepository: WeatherRepository) {
    fun startTracking(lifecycleOwner: LifecycleOwner) {
        weatherRepository.trackDeviceLocation(lifecycleOwner)
    }
}