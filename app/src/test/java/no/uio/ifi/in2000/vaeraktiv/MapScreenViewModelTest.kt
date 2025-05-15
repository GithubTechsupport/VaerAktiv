package no.uio.ifi.in2000.vaeraktiv

import no.uio.ifi.in2000.vaeraktiv.ui.map.MapScreenViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import no.uio.ifi.in2000.vaeraktiv.data.weather.IAggregateRepository
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlaceActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.osmdroid.util.GeoPoint

@RunWith(MockitoJUnitRunner::class)
class MapScreenViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    lateinit var aggregateRepository: IAggregateRepository

    private lateinit var viewModel: MapScreenViewModel

    @Before
    fun setup() {
        `when`(aggregateRepository.activities).thenReturn(MutableLiveData(emptyList()))
        viewModel = MapScreenViewModel(aggregateRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun updatePlacesAndRoutes_filtersCustomAndNullEntries() = runTest {
        // valid place
        val place = PlaceActivitySuggestion(
            month = 6, dayOfMonth = 15,
            timeStart = "09:00", timeEnd = "10:00",
            activityName = "Lake Hike", activityDesc = "Morning hike",
            id = "pX", placeName = "Mirror Lake",
            formattedAddress = "Mountain Rd", coordinates = Pair(47.6, -122.3)
        )
        // valid route
        val route = StravaActivitySuggestion(
            month = 6, dayOfMonth = 15,
            timeStart = "11:00", timeEnd = "12:30",
            activityName = "Trail Run", activityDesc = "Forest loop",
            id = "rX", routeName = "Woodland", distance = 8.5,
            polyline = "_p~iF~ps|U_ulLnnqC_mqNvxq`@"
        )
        // custom suggestion to be filtered out
        val custom = CustomActivitySuggestion(
            month = 6, dayOfMonth = 15,
            timeStart = "13:00", timeEnd = "14:00",
            activityName = "Yoga", activityDesc = "Afternoon session"
        )
        // suggestions lists with mixed types and a null entry
        val validList = SuggestedActivities(listOf(place, custom))
        val secondList = SuggestedActivities(listOf(custom, route))
        val onlyCustomList = SuggestedActivities(listOf(custom))

        viewModel.updatePlacesAndRoutes(listOf(validList, secondList, onlyCustomList, null))
        advanceUntilIdle()

        val state = viewModel.mapScreenUiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.places.size)
        assertEquals(place, state.places.first())
        assertEquals(1, state.routes.size)
        assertEquals(route, state.routes.first())
    }

    @Test
    fun zoomInOnPlace_setsSelectedPoints() {
        val place = PlaceActivitySuggestion(
            month = 2, dayOfMonth = 5,
            timeStart = "07:30", timeEnd = "08:30",
            activityName = "Morning Jog", activityDesc = "Sunrise run",
            id = "p2", placeName = "Beach", formattedAddress = "Coast Rd",
            coordinates = Pair(34.01, -118.49)
        )

        viewModel.zoomInOnActivity(place)
        val pts = viewModel.mapScreenUiState.value.selectedActivityPoints
        assertNotNull(pts)
        assertEquals(1, pts!!.size)
        assertEquals(34.01, pts[0].latitude, 0.0)
        assertEquals(-118.49, pts[0].longitude, 0.0)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun zoomInOnStrava_setsDecodedPolylinePoints() = runTest {
        val encoded = "_p~iF~ps|U_ulLnnqC_mqNvxq`@"
        val expected = listOf(
            GeoPoint(38.5, -120.2),
            GeoPoint(40.7, -120.95),
            GeoPoint(43.252, -126.453)
        )
        val route = StravaActivitySuggestion(
            month = 3, dayOfMonth = 10,
            timeStart = "12:00", timeEnd = "14:00",
            activityName = "Mountain Loop", activityDesc = "Trail ride",
            id = "r2", routeName = "Summit", distance = 20.0,
            polyline = encoded
        )

        viewModel.zoomInOnActivity(route)
        advanceUntilIdle()

        val pts = viewModel.mapScreenUiState.value.selectedActivityPoints
        assertNotNull(pts)
        assertEquals(expected.size, pts!!.size)
        expected.forEachIndexed { i, gp ->
            assertEquals(gp.latitude, pts[i].latitude, 1e-4)
            assertEquals(gp.longitude, pts[i].longitude, 1e-4)
        }
    }

    @Test
    fun clearSelectedActivityPoints_setsNull() {
        val place = PlaceActivitySuggestion(
            month = 4, dayOfMonth = 12,
            timeStart = "09:00", timeEnd = "10:00",
            activityName = "City Tour", activityDesc = "Walking tour",
            id = "p3", placeName = "Downtown", formattedAddress = "Main St",
            coordinates = Pair(51.51, -0.13)
        )

        viewModel.zoomInOnActivity(place)
        viewModel.clearSelectedActivityPoints()
        assertNull(viewModel.mapScreenUiState.value.selectedActivityPoints)
    }
}