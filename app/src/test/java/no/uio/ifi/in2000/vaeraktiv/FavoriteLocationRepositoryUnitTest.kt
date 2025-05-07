package no.uio.ifi.in2000.vaeraktiv

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationDataSource
import no.uio.ifi.in2000.vaeraktiv.data.location.FavoriteLocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
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
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

class FavoriteLocationRepositoryTest {

    private lateinit var dataSource: FavoriteLocationDataSource
    private lateinit var geocoder: GeocoderClass
    private lateinit var repository: FavoriteLocationRepository

    @Before
    fun setup() {
        dataSource = mock(FavoriteLocationDataSource::class.java)
        geocoder = mock(GeocoderClass::class.java)
        repository = FavoriteLocationRepository(dataSource, geocoder)
    }

    @Test
    fun `addLocationByName formats coordinates and adds location`() {
        // Arrange
        val placeName = "Stockholm, Sweden"
        val expectedName = "Stockholm"
        val latitude = 59.334591
        val longitude = 18.063240
        val formattedLat = 59.335
        val formattedLng = 18.063

        `when`(geocoder.getCoordinatesFromLocation(placeName))
            .thenReturn(Pair(placeName, Pair(latitude, longitude)))

        // Act
        repository.addLocationByName(placeName)

        // Assert
        verify(dataSource).addLocation(expectedName, formattedLat, formattedLng)
    }

    @Test
    fun `addLocationByName does not add location when geocoder returns null`() {
        // Arrange
        val placeName = "Unknown Place"
        `when`(geocoder.getCoordinatesFromLocation(placeName)).thenReturn(null)

        // Act
        repository.addLocationByName(placeName)

        // Assert
        verify(dataSource, never()).addLocation(anyString(), anyDouble(), anyDouble())
    }

    @Test
    fun `addLocationByName uses full name if no comma`() {
        val placeName = "Oslo"
        `when`(geocoder.getCoordinatesFromLocation(placeName))
            .thenReturn(Pair("Oslo", Pair(57.708, 11.974)))

        repository.addLocationByName(placeName)

        verify(dataSource).addLocation("Oslo", 57.708, 11.974)
    }

    @Test
    fun `addLocationByName does not add if geocoder returns null coordinates`() {
        val placeName = "Nowhere"
        `when`(geocoder.getCoordinatesFromLocation(placeName)).thenReturn(null)

        repository.addLocationByName(placeName)

        verify(dataSource, never()).addLocation(anyString(), anyDouble(), anyDouble())
    }

    @Test
    fun `getAllLocations returns values from data source`() {
        val expected = listOf("Oslo", "Bergen")
        `when`(dataSource.getAllLocations()).thenReturn(expected)

        val result = repository.getAllLocations()

        assertEquals(expected, result)
    }
}