package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class SunriseRepositoryUnitTest {

    private lateinit var dataSource: SunriseDataSource
    private lateinit var repository: SunriseRepository

    @Before
    fun setup() {
        dataSource = mock()
        repository = SunriseRepository(dataSource)
    }

    @Test
    fun `getSunriseTime returns actual times when data source is non-null`() = runTest {
        val sunrise = SunEvent("07:00", 0.0)
        val sunset = SunEvent("19:00", 0.0)
        val props = SunProperties("body", sunrise, sunset, SolarEvent("12:00", true), SolarEvent("00:00", false))
        val sunData = SunData("", "", "", SunGeometry("", listOf()), SunTimeInterval(listOf()), props)

        whenever(dataSource.getSunrise("1.0", "2.0", "2024-01-01")).thenReturn(sunData)

        val times = repository.getSunriseTime("1.0", "2.0", "2024-01-01")

        assertEquals(listOf("07:00", "19:00"), times)
    }

    @Test
    fun `getSunriseTime returns defaults when data source is null`() = runTest {
        whenever(dataSource.getSunrise(any(), any(), any())).thenReturn(null)

        val times = repository.getSunriseTime("0", "0", "2024-01-01")

        assertEquals(
            listOf("No sunrise data found", "No sunset data found"),
            times
        )
    }
}