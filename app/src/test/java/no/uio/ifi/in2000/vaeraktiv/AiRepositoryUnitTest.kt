import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlaceActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AiRepositoryUnitTest {

    private lateinit var repository: AiRepository
    private lateinit var client: AiClient
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        client = mock()
        repository = AiRepository(client)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // getSingleSuggestionForDay tests

    @Test
    fun `getSingleSuggestionForDay deserializes PlacesActivitySuggestion`() = runTest(testDispatcher) {
        val json = """
        {
          "type": "PlaceActivitySuggestion",
          "month": 8,
          "dayOfMonth": 20,
          "timeStart": "09:00",
          "timeEnd": "11:00",
          "activityName": "City Tour",
          "activityDesc": "Tour the city center",
          "id": "p01",
          "placeName": "Old Town",
          "formattedAddress": "Main St",
          "coordinates": {"first":50.0, "second":11.0}
        }
        """.trimIndent()
        whenever(client.getSingleSuggestionForDay(any(), any(), any(), any(), any())).thenReturn(json)
        val result = repository.getSingleSuggestionForDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        assertTrue(result is PlaceActivitySuggestion)
        result as PlaceActivitySuggestion
        assertEquals("City Tour", result.activityName)
        assertEquals(50.0, result.coordinates.first, 0.0)
    }

    @Test
    fun `getSingleSuggestionForDay deserializes StravaActivitySuggestion`() = runTest(testDispatcher) {
        val json = """
        {
          "type": "StravaActivitySuggestion",
          "month": 9,
          "dayOfMonth": 5,
          "timeStart": "14:00",
          "timeEnd": "16:00",
          "activityName": "Trail Ride",
          "activityDesc": "Mountain biking",
          "id": "s01",
          "routeName": "Summit Loop",
          "distance": 20.0,
          "polyline": "xyz"
        }
        """.trimIndent()
        whenever(client.getSingleSuggestionForDay(any(), any(), any(), any(), any())).thenReturn(json)
        val result = repository.getSingleSuggestionForDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        assertTrue(result is StravaActivitySuggestion)
        result as StravaActivitySuggestion
        assertEquals("Trail Ride", result.activityName)
        assertEquals(20.0, result.distance, 0.0)
    }

    @Test
    fun `getSingleSuggestionForDay deserializes CustomActivitySuggestion`() = runTest(testDispatcher) {
        val json = """
        {
          "type": "CustomActivitySuggestion",
          "month": 7,
          "dayOfMonth": 15,
          "timeStart": "12:00",
          "timeEnd": "13:00",
          "activityName": "Solo Hike",
          "activityDesc": "Hike alone"
        }
        """.trimIndent()
        whenever(client.getSingleSuggestionForDay(any(), any(), any(), any(), any())).thenReturn(json)
        val result = repository.getSingleSuggestionForDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        assertTrue(result is CustomActivitySuggestion)
        result as CustomActivitySuggestion
        assertEquals("Solo Hike", result.activityName)
    }

    // getSuggestionsForOneDay tests

    @Test
    fun `getSuggestionsForOneDay deserializes PlacesActivitySuggestion`() = runTest(testDispatcher) {
        val fake = """
        {
          "activities": [
            {
              "type": "PlaceActivitySuggestion",
              "month": 8,
              "dayOfMonth": 20,
              "timeStart": "09:00",
              "timeEnd": "11:00",
              "activityName": "City Tour",
              "activityDesc": "Tour the city center",
              "id": "p01",
              "placeName": "Old Town",
              "formattedAddress": "Main St",
              "coordinates": {"first":50.0, "second":11.0}
            }
          ]
        }
        """.trimIndent()
        whenever(client.getSuggestionsForOneDay(any(), any(), any(), any(), any())).thenReturn(fake)
        val result = repository.getSuggestionsForOneDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        val activity = result.activities.first()
        assertTrue(activity is PlaceActivitySuggestion)
    }

    @Test
    fun `getSuggestionsForOneDay deserializes StravaActivitySuggestion`() = runTest(testDispatcher) {
        val fake = """
        {
          "activities": [
            {
              "type": "StravaActivitySuggestion",
              "month": 9,
              "dayOfMonth": 5,
              "timeStart": "14:00",
              "timeEnd": "16:00",
              "activityName": "Trail Ride",
              "activityDesc": "Mountain biking",
              "id": "s01",
              "routeName": "Summit Loop",
              "distance": 20.0,
              "polyline": "xyz"
            }
          ]
        }
        """.trimIndent()
        whenever(client.getSuggestionsForOneDay(any(), any(), any(), any(), any())).thenReturn(fake)
        val result = repository.getSuggestionsForOneDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        val activity = result.activities.first()
        assertTrue(activity is StravaActivitySuggestion)
    }

    @Test
    fun `getSuggestionsForOneDay deserializes CustomActivitySuggestion`() = runTest(testDispatcher) {
        val fake = """
        {
          "activities": [
            {
              "type": "CustomActivitySuggestion",
              "month": 7,
              "dayOfMonth": 15,
              "timeStart": "12:00",
              "timeEnd": "13:00",
              "activityName": "Solo Hike",
              "activityDesc": "Hike alone"
            }
          ]
        }
        """.trimIndent()
        whenever(client.getSuggestionsForOneDay(any(), any(), any(), any(), any())).thenReturn(fake)
        val result = repository.getSuggestionsForOneDay(
            mock<FormattedForecastDataForPrompt>(),
            mock<NearbyPlacesSuggestions>(),
            mock<RoutesSuggestions>(),
            "prefs"
        )
        val activity = result.activities.first()
        assertTrue(activity is CustomActivitySuggestion)
    }
}