package no.uio.ifi.in2000.vaeraktiv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlacesActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

@ExperimentalCoroutinesApi
class AiRepositoryTest {

    private lateinit var repository: AiRepository
    private lateinit var client: AiClient

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        client = mock(AiClient::class.java)
        repository = AiRepository(client)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getSuggestionsForOneDay returns decoded list`() = runTest(testDispatcher) {
        // Arrange
        val fakeResponse = """
            {
                  "activities": [
                        {
                          "type": "PlacesActivitySuggestion",
                          "month": 5,
                          "dayOfMonth": 3,
                          "timeStart": "10:00",
                          "timeEnd": "12:00",
                          "activityName": "Park Walk",
                          "activityDesc": "Walk in the park",
                          "id": "1",
                          "placeName": "Central Park",
                          "formattedAddress": "123 Street",
                          "coordinates": [37.0, -122.0]
                        }
                  ]
            }
        """.trimIndent()

        `when`(client.getSuggestionsForOneDay(any(), any(), any(), any(), any()))
            .thenReturn(fakeResponse)

        // Act
        val result = repository.getSuggestionsForOneDay(
            mock(FormattedForecastDataForPrompt::class.java), mock(NearbyPlacesSuggestions::class.java), mock(
                RoutesSuggestions::class.java), "pref"
        )

        // Assert
        assertEquals(1, result.activities.size)
        val suggestion = result.activities[0] as PlacesActivitySuggestion
        assertEquals("Park Walk", suggestion.activityName)
    }

    @Test
    fun `getSingleSuggestionForDay returns decoded suggestion`() = runTest(testDispatcher) {
        // Arrange
        val fakeResponse = """
            {
                  "activities": [
                        {
                          "type": "PlacesActivitySuggestion",
                          "month": 5,
                          "dayOfMonth": 3,
                          "timeStart": "10:00",
                          "timeEnd": "12:00",
                          "activityName": "Park Walk",
                          "activityDesc": "Walk in the park",
                          "id": "1",
                          "placeName": "Central Park",
                          "formattedAddress": "123 Street",
                          "coordinates": [37.0, -122.0]
                        }
                  ]
            }
        """.trimIndent()

        `when`(client.getSingleSuggestionForDay(any(), any(), any(), any(), any()))
            .thenReturn(fakeResponse)

        // Act
        val result = repository.getSingleSuggestionForDay(
            mock(FormattedForecastDataForPrompt::class.java), mock(NearbyPlacesSuggestions::class.java), mock(
                RoutesSuggestions::class.java), "pref"
        )

        // Assert
        assert(result is CustomActivitySuggestion)
        assertEquals("Custom Fun", result.activityName)
    }
}