package no.uio.ifi.in2000.vaeraktiv

import androidx.lifecycle.LifecycleOwner
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify



@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelUnitTest {
    private val weatherRepository: WeatherRepository = mock()
    private val deviceDateTimeRepository: DeviceDateTimeRepository = mock()
    private val viewModel: HomeScreenViewModel = HomeScreenViewModel(
        weatherRepository,
        deviceDateTimeRepository
    )

    @Test
    fun testLocationTracking() {
        val mockLifeCycleOwner = mock<LifecycleOwner>()
        viewModel.startTracking(mockLifeCycleOwner)
        verify(weatherRepository).trackDeviceLocation(mockLifeCycleOwner)
    }

}