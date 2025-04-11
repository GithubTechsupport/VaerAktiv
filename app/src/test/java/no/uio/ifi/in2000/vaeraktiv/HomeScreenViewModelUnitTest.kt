package no.uio.ifi.in2000.vaeraktiv

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelUnitTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    // Dependencies
    private val weatherRepository: WeatherRepository = mock()
    private val deviceDateTimeRepository: DeviceDateTimeRepository = mock()
    private lateinit var viewModel: HomeScreenViewModel

    // Test Data
    private val testLocation = Location("Test Location", "0.0", "0.0")
    private val testForecastToday = ForecastToday("Sunny", "20", "10")
    private val testForecastForDay = listOf(ForecastForDay("Monday", "20", "Sunny"))
    private val testAlerts = mutableListOf(AlertData("Flood Warning", "Heavy rain expected"))
    private val testSunRiseSet = listOf("06:00", "20:00")
    private val testDateTime = "2024-01-01T12:00:00Z"
    private val mutableLiveData = MutableLiveData<Location?>()

    @Before
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        mutableLiveData.value = testLocation
        whenever(weatherRepository.currentLocation).thenReturn(mutableLiveData)

        viewModel = HomeScreenViewModel(weatherRepository, deviceDateTimeRepository)

        whenever(weatherRepository.getForecastToday(any())).thenReturn(testForecastToday)
        whenever(weatherRepository.getForecastByDay(any())).thenReturn(testForecastForDay)
        whenever(weatherRepository.getAlertsForLocation(any())).thenReturn(testAlerts)
        whenever(weatherRepository.getSunRiseData(any(), any())).thenReturn(testSunRiseSet)

        whenever(deviceDateTimeRepository.getDateTime()).thenReturn(testDateTime)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * In each test below we override one default whenever call to throw an exception.
     * That way every other data method returns the default successful value.
     */

    @Test
    fun `getHomeScreenData handles exception in getForecastToday`() = runTest {
        // Given: override the default for getForecastToday to throw an exception.
        val exception = RuntimeException("Failed to fetch today's forecast")
        whenever(weatherRepository.getForecastToday(any())).thenThrow(exception)

        // When
        viewModel.getHomeScreenData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: assert that only todaysWeather is affected
        val uiState = viewModel.homeScreenUiState.first()
        assertNotNull(uiState.todaysWeatherError)
        assertEquals(exception.toString(), uiState.todaysWeatherError)
        assertNull(uiState.todaysWeather)

        // Other data should succeed
        assertEquals(testForecastForDay, uiState.thisWeeksWeather)
        assertEquals(testAlerts, uiState.alerts)
        assertEquals(testSunRiseSet, uiState.sunRiseSet)
    }

    @Test
    fun `getHomeScreenData handles exception in getForecastByDay`() = runTest {
        // Given: override the default for getForecastByDay to throw an exception.
        val exception = RuntimeException("Failed to fetch weekly forecast")
        whenever(weatherRepository.getForecastByDay(any())).thenThrow(exception)

        // When
        viewModel.getHomeScreenData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: assert that only thisWeeksWeather is affected
        val uiState = viewModel.homeScreenUiState.first()
        assertNotNull(uiState.thisWeeksWeatherError)
        assertEquals(exception.toString(), uiState.thisWeeksWeatherError)
        assertEquals(emptyList<ForecastForDay>(), uiState.thisWeeksWeather)

        // Other data should succeed
        assertEquals(testForecastToday, uiState.todaysWeather)
        assertEquals(testAlerts, uiState.alerts)
        assertEquals(testSunRiseSet, uiState.sunRiseSet)
    }

    @Test
    fun `getHomeScreenData handles exception in getAlertsForLocation`() = runTest {
        // Given: override the default for getAlertsForLocation to throw an exception.
        val exception = RuntimeException("Failed to fetch alerts")
        whenever(weatherRepository.getAlertsForLocation(any())).thenThrow(exception)

        // When
        viewModel.getHomeScreenData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: assert that only alerts are affected
        val uiState = viewModel.homeScreenUiState.first()
        assertNotNull(uiState.alertsError)
        assertEquals(exception.toString(), uiState.alertsError)
        assertEquals(emptyList<AlertData>(), uiState.alerts)

        // Other data should succeed
        assertEquals(testForecastToday, uiState.todaysWeather)
        assertEquals(testForecastForDay, uiState.thisWeeksWeather)
        assertEquals(testSunRiseSet, uiState.sunRiseSet)
    }

    @Test
    fun `getHomeScreenData handles exception in getDateTime & getSunRiseData handling`() = runTest {
        // Option A: Testing exception when getDateTime fails.
        // Given: override the default for getDateTime to throw an exception.
        val exception = RuntimeException("Failed to fetch device date time")
        whenever(deviceDateTimeRepository.getDateTime()).thenThrow(exception)

        // When
        viewModel.getHomeScreenData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then: verify that the error for sunRiseSet is set.
        val uiState = viewModel.homeScreenUiState.first()
        assertNotNull(uiState.sunRiseSetError)
        assertEquals(exception.toString(), uiState.sunRiseSetError)
        assertEquals(emptyList<String>(), uiState.sunRiseSet)

        // Other data should succeed
        assertEquals(testForecastToday, uiState.todaysWeather)
        assertEquals(testForecastForDay, uiState.thisWeeksWeather)
        assertEquals(testAlerts, uiState.alerts)
    }

    @Test
    fun `startTracking calls trackDeviceLocation on WeatherRepository`() {
        // Given
        val lifecycleOwner: LifecycleOwner = mock()

        // When
        viewModel.startTracking(lifecycleOwner)

        // Then
        verify(weatherRepository).trackDeviceLocation(lifecycleOwner)
    }
}
