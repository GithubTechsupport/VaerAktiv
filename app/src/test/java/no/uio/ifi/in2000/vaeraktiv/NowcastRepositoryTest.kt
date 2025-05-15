package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.vaeraktiv.model.nowcast.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class NowcastRepositoryTest {

    private lateinit var dataSource: NowcastDataSource
    private lateinit var repository: NowcastRepository
    private val lat = "1.0"
    private val lon = "2.0"
    private val dummyResponse = NowcastResponse(
        type = "T",
        geometry = LGeometry("G", listOf(0.0, 0.0)),
        properties = Properties(Meta("2024-01-01T00:00:00Z", Units("C","mm","mm/h","%","°","m/s","m/s")), emptyList())
    )

    @Before
    fun setup() {
        dataSource = mock()
        repository = NowcastRepository(dataSource)
    }

    @Test
    fun `getUpdate fetches and caches response`() = runTest {
        whenever(dataSource.getResponse(any())).thenReturn(dummyResponse)

        val first = repository.getUpdate(lat, lon)
        val second = repository.getForecast(lat, lon)

        assertSame(dummyResponse, first)
        assertSame(first, second)
        verify(dataSource, times(1)).getResponse(any())
    }

    @Test
    fun `getForecast without cache calls getUpdate`() = runTest {
        whenever(dataSource.getResponse(any())).thenReturn(dummyResponse)

        val result = repository.getForecast(lat, lon)
        assertSame(dummyResponse, result)
        verify(dataSource).getResponse(any())
    }
}