//package no.uio.ifi.in2000.vaeraktiv.ui.home
//
//import androidx.lifecycle.MutableLiveData
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepository
//import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepository
//import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
//import no.uio.ifi.in2000.vaeraktiv.model.ui.AlertData
//import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForDay
//import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastForHour
//import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
//import org.junit.After
//import org.junit.Assert.*
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.junit.MockitoJUnitRunner
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.whenever
//import java.time.LocalDateTime
//
//@RunWith(MockitoJUnitRunner::class)
//@OptIn(ExperimentalCoroutinesApi::class)
//class HomeScreenViewModelUnitTests {
//    private val dispatcher = StandardTestDispatcher()
//
//    private val dummyLocation = Location("test", "0.0", "0.0")
//    private val dummyForecastToday: ForecastToday = mock()
//    private val dummyForecastByDay: List<ForecastForDay> = listOf(mock())
//    private val dummyAlerts: List<AlertData> = listOf(mock())
//    private val dummySunrise = listOf("06:00", "18:00")
//    private val dummyHourly: List<ForecastForHour> = listOf(mock())
//    private val dummyDateTime = LocalDateTime.now()
//
//    private lateinit var weatherRepository: WeatherRepository
//    private lateinit var dateTimeRepository: DeviceDateTimeRepository
//
//    @Before
//    fun setup() {
//        Dispatchers.setMain(dispatcher)
//        weatherRepository = mock()
//        dateTimeRepository = mock()
//        whenever(weatherRepository.currentLocation)
//            .thenReturn(MutableLiveData(dummyLocation))
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun initialState_isDefault() {
//        val vm = HomeScreenViewModel(weatherRepository, dateTimeRepository)
//        val state = vm.homeScreenUiState.value
//
//        assertFalse(state.isLoading)
//        assertEquals("", state.locationName)
//        assertTrue(state.alerts.isEmpty())
//        assertNull(state.weatherToday)
//        assertTrue(state.thisWeeksWeather.isEmpty())
//        assertTrue(state.sunRiseSet.isEmpty())
//        assertTrue(state.next24Hours.isEmpty())
//        assertNull(state.todaysWeatherError)
//        assertFalse(state.isLoadingActivitiesToday)
//        assertFalse(state.isErrorActivitiesToday)
//    }
//
//    @Test
//    fun getHomeScreenData_success_populatesAllFields() = runTest(dispatcher) {
//        whenever(weatherRepository.getForecastToday(dummyLocation))
//            .thenReturn(dummyForecastToday)
//        whenever(weatherRepository.getForecastByDay(dummyLocation))
//            .thenReturn(dummyForecastByDay)
//        whenever(weatherRepository.getAlertsForLocation(dummyLocation))
//            .thenReturn(dummyAlerts)
//        whenever(dateTimeRepository.getDateTime())
//            .thenReturn(dummyDateTime)
//        whenever(weatherRepository.getSunRiseData(dummyLocation, dummyDateTime))
//            .thenReturn(dummySunrise)
//        whenever(weatherRepository.getForecastForHour(dummyLocation))
//            .thenReturn(dummyHourly)
//
//        val vm = HomeScreenViewModel(weatherRepository, dateTimeRepository)
//        vm.getHomeScreenData()
//        dispatcher.scheduler.advanceUntilIdle()
//
//        val state = vm.homeScreenUiState.value
//        assertEquals(dummyForecastToday, state.weatherToday)
//        assertEquals(dummyForecastByDay, state.thisWeeksWeather)
//        assertEquals(dummyAlerts, state.alerts)
//        assertEquals(dummySunrise, state.sunRiseSet)
//        assertEquals(dummyHourly, state.next24Hours)
//        assertNull(state.todaysWeatherError)
//        assertNull(state.thisWeeksWeatherError)
//        assertNull(state.alertsError)
//        assertNull(state.sunRiseSetError)
//        assertNull(state.next24HoursError)
//        assertFalse(state.isLoading)
//    }
//
//    @Test
//    fun getHomeScreenData_forecastTodayError_setsErrorOnlyForToday() = runTest(dispatcher) {
//        whenever(weatherRepository.getForecastToday(dummyLocation))
//            .thenThrow(RuntimeException("fail"))
//        whenever(weatherRepository.getForecastByDay(dummyLocation))
//            .thenReturn(dummyForecastByDay)
//        whenever(weatherRepository.getAlertsForLocation(dummyLocation))
//            .thenReturn(dummyAlerts)
//        whenever(dateTimeRepository.getDateTime())
//            .thenReturn(dummyDateTime)
//        whenever(weatherRepository.getSunRiseData(dummyLocation, dummyDateTime))
//            .thenReturn(dummySunrise)
//        whenever(weatherRepository.getForecastForHour(dummyLocation))
//            .thenReturn(dummyHourly)
//
//        val vm = HomeScreenViewModel(weatherRepository, dateTimeRepository)
//        vm.getHomeScreenData()
//        dispatcher.scheduler.advanceUntilIdle()
//
//        val state = vm.homeScreenUiState.value
//        assertEquals("java.lang.RuntimeException: fail", state.todaysWeatherError)
//        assertNull(state.weatherToday)
//        // all other data should still load
//        assertEquals(dummyForecastByDay, state.thisWeeksWeather)
//        assertEquals(dummyAlerts, state.alerts)
//        assertEquals(dummySunrise, state.sunRiseSet)
//        assertEquals(dummyHourly, state.next24Hours)
//    }
//}