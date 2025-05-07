package no.uio.ifi.in2000.vaeraktiv
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.*
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import org.junit.Before
import org.junit.*
import org.mockito.kotlin.*


@OptIn(ExperimentalCoroutinesApi::class)
class LocationForecastRepositoryTest {

    private lateinit var repository: LocationForecastRepository
    private val mockDataSource: LocationForecastDataSource = mock()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = LocationForecastRepository(mockDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUpdate stores and returns response`() = runTest {
        val lat = "60.0"
        val lon = "10.0"
        val mockResponse = mock<LocationForecastResponse>()
        whenever(mockDataSource.getResponse(any())).thenReturn(mockResponse)

        val result = repository.getUpdate(lat, lon)

        assertEquals(mockResponse, result)
    }

    @Test
    fun `getForecast fetches and caches if not already cached`() = runTest {
        val lat = "60.0"
        val lon = "10.0"
        val mockResponse = mock<LocationForecastResponse>()
        whenever(mockDataSource.getResponse(any())).thenReturn(mockResponse)

        // First call should fetch from dataSource
        val result1 = repository.getForecast(lat, lon)
        // Second call should use cached value
        val result2 = repository.getForecast(lat, lon)

        verify(mockDataSource, times(1)).getResponse(any())
        assertEquals(mockResponse, result1)
        assertEquals(mockResponse, result2)
    }




}