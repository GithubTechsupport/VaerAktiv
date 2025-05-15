package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaDatasource
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.ai.RouteSource
import no.uio.ifi.in2000.vaeraktiv.model.strava.ExplorerSegment
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class StravaRepositoryUnitTest {

    private lateinit var dataSource: StravaDatasource
    private lateinit var repository: StravaRepository
    private val location = Location("Test", "1.0", "2.0")

    @Before
    fun setup() {
        dataSource = mock()
        repository = StravaRepository(dataSource)
    }

    @Test
    fun `getRouteSuggestions maps segments correctly`() = runTest {
        val segment = ExplorerSegment(
            id = 42L,
            name = "Trail",
            distance = 1000.0,
            averageGrade = 5.0,
            elevationGain = 100.0,
            polyline = "abc",
            startPosition = listOf(1.1, 2.2),
            endPosition = listOf(3.3, 4.4)
        )
        whenever(dataSource.fetchPopularRunSegments(any(), any(), any(), any()))
            .thenReturn(listOf(segment))

        val result = repository.getRouteSuggestions(location)

        assertNotNull(result)
        val suggestions = result.suggestions
        assertEquals(1, suggestions.size)
        val s = suggestions.first()
        assertEquals("42", s.id)
        assertEquals("Trail", s.routeName)
        assertEquals(1000.0, s.distance, 0.0)
        assertEquals(RouteSource.STRAVA, s.source)
        assertEquals(Pair(1.1, 2.2), s.startPosition)
        assertEquals(Pair(3.3, 4.4), s.endPosition)
    }

    @Test
    fun `getRouteSuggestions returns empty when no segments`() = runTest {
        whenever(dataSource.fetchPopularRunSegments(any(), any(), any(), any()))
            .thenReturn(emptyList())

        val result = repository.getRouteSuggestions(location)

        assertTrue(result.suggestions.isEmpty())
    }
}