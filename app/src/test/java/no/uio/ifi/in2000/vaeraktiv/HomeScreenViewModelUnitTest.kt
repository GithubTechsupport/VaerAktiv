package no.uio.ifi.in2000.vaeraktiv
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.home.*
import no.uio.ifi.in2000.vaeraktiv.ui.home.HomeScreenViewModel
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelUnitTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var aggregateRepository: IAggregateRepository
    private lateinit var deviceDateTimeRepository: DeviceDateTimeRepository
    private lateinit var viewModel: HomeScreenViewModel
    private val location = Location("TestPlace", "1.0", "2.0")

    private val todayForecast = ForecastToday("now","max","min","uv","wind","precip","ic","icNow")
    private val weekForecast = listOf(ForecastForDay("d1","m1","i1"), ForecastForDay("d2","m2","i2"))
    private val alertsList = mutableListOf(AlertData("area","type","desc","event","inst","color","contact"))
    private val sunRiseSet = listOf("06:00","18:00")
    private val hours = listOf(ForecastForHour("10","20","w","p","ic","uv"))
    private val intervals = listOf(listOf(DetailedForecastForDay("d","int","ic")))

    @Before
    fun setup() = runTest {
        Dispatchers.setMain(testDispatcher)
        aggregateRepository = mock()
        deviceDateTimeRepository = mock()
        // mock current location LiveData
        whenever(aggregateRepository.currentLocation)
            .thenReturn(MutableLiveData(location))
        // stub normal repository calls
        whenever(aggregateRepository.getForecastToday(location)).thenReturn(todayForecast)
        whenever(aggregateRepository.getForecastByDay(location)).thenReturn(weekForecast)
        whenever(aggregateRepository.getAlertsForLocation(location)).thenReturn(alertsList)
        whenever(deviceDateTimeRepository.getDateTime()).thenReturn("2024-01-01")
        whenever(aggregateRepository.getSunRiseData(location, "2024-01-01")).thenReturn(sunRiseSet)
        whenever(aggregateRepository.getForecastForHour(location)).thenReturn(hours)
        whenever(aggregateRepository.getForecastByDayIntervals(location)).thenReturn(intervals)

        viewModel = HomeScreenViewModel(aggregateRepository, deviceDateTimeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getHomeScreenData_success_updatesAllUiState() = runTest {
        viewModel.getHomeScreenData()
        testScheduler.advanceUntilIdle()

        val ui = viewModel.homeScreenUiState.value
        assertFalse(ui.isLoading)
        assertEquals(todayForecast, ui.weatherToday)
        assertNull(ui.weatherTodayError)
        assertEquals(weekForecast, ui.thisWeeksWeather)
        assertEquals(alertsList, ui.alerts)
        assertEquals(sunRiseSet, ui.sunRiseSet)
        assertEquals(hours, ui.next24Hours)
        assertEquals(intervals, ui.dayIntervals)
        assertEquals(location.addressName, ui.locationName)
    }

    @Test
    fun getHomeScreenData_todayForecastThrows_setsErrorOnlyForToday() = runTest {
        val ex = RuntimeException("today fail")
        whenever(aggregateRepository.getForecastToday(location)).thenThrow(ex)

        viewModel.getHomeScreenData()
        testScheduler.advanceUntilIdle()

        val ui = viewModel.homeScreenUiState.value
        assertNull(ui.weatherToday)
        assertEquals(ex.toString(), ui.weatherTodayError)
        // other data still loaded
        assertEquals(weekForecast, ui.thisWeeksWeather)
        assertEquals(alertsList, ui.alerts)
    }

    @Test
    fun getHomeScreenData_weeklyForecastThrows_setsErrorOnlyForWeek() = runTest {
        val ex = RuntimeException("week fail")
        whenever(aggregateRepository.getForecastByDay(location)).thenThrow(ex)

        viewModel.getHomeScreenData()
        testScheduler.advanceUntilIdle()

        val ui = viewModel.homeScreenUiState.value
        assertNull(ui.weatherTodayError) // today still ok
        assertEquals(ex.toString(), ui.thisWeeksWeatherError)
        assertTrue(ui.thisWeeksWeather.isEmpty())
        // alerts still loaded
        assertEquals(alertsList, ui.alerts)
    }

    @Test
    fun getHomeScreenData_alertsThrows_setsErrorOnlyForAlerts() = runTest {
        val ex = RuntimeException("alerts fail")
        whenever(aggregateRepository.getAlertsForLocation(location)).thenThrow(ex)

        viewModel.getHomeScreenData()
        testScheduler.advanceUntilIdle()

        val ui = viewModel.homeScreenUiState.value
        assertNull(ui.thisWeeksWeatherError)
        assertEquals(ex.toString(), ui.alertsError)
        assertTrue(ui.alerts.isEmpty())
        // today and week still loaded
        assertEquals(todayForecast, ui.weatherToday)
        assertEquals(weekForecast, ui.thisWeeksWeather)
    }
}