package no.uio.ifi.in2000.vaeraktiv

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.vaeraktiv.data.ai.AiRepository
import no.uio.ifi.in2000.vaeraktiv.data.location.GeocoderClass
import no.uio.ifi.in2000.vaeraktiv.data.location.LocationRepository
import no.uio.ifi.in2000.vaeraktiv.data.places.PlacesRepository
import no.uio.ifi.in2000.vaeraktiv.data.strava.StravaRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.WeatherRepositoryDefault
import no.uio.ifi.in2000.vaeraktiv.data.weather.alerts.MetAlertsRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast.NowcastRepository
import no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise.SunriseRepository
import no.uio.ifi.in2000.vaeraktiv.data.welcome.PreferenceRepository
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Features
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Properties
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class WeatherRepositoryDefaultTest {

    // Mocks
    private val metAlertsRepository: MetAlertsRepository = mock(MetAlertsRepository::class.java)

    // Create other mocks if needed but we can use nulls or relaxed fakes for unused deps
    private val weatherRepository = WeatherRepositoryDefault(
        metAlertsRepository = metAlertsRepository,
        locationForecastRepository = mock(LocationForecastRepository::class.java),
        sunriseRepository = mock(SunriseRepository::class.java),
        aiRepository = mock(AiRepository::class.java),
        deviceLocationRepository = mock(LocationRepository::class.java),
        geocoderClass = mock(GeocoderClass::class.java),
        nowcastRepository = mock(NowcastRepository::class.java),
        placesRepository = mock(PlacesRepository::class.java),
        stravaRepository = mock(StravaRepository::class.java),
        preferenceRepository = mock(PreferenceRepository::class.java)
    )

    @Test
    fun `getAlertsForLocation should return mapped AlertData list from response`() = runBlocking {
        // Arrange
        val testLocation = Location("Bergen", 60.391.toString(), 5.322.toString())

        val fakeProperties = mock(Properties::class.java).apply {
            `when`(area).thenReturn("Test Area")
            `when`(awarenessType).thenReturn("Type A")
            `when`(description).thenReturn("Heavy Rainfall")
            `when`(eventAwarenessName).thenReturn("Rain Warning")
            `when`(instruction).thenReturn("Stay indoors")
            `when`(riskMatrixColor).thenReturn("Red")
            `when`(contact).thenReturn("123-456")
        }

        val fakeFeature = mock(Features::class.java).apply {
            `when`(properties).thenReturn(fakeProperties)
        }

        `when`(metAlertsRepository.getAlertsForLocation(60.391.toString(), 5.322.toString())).thenReturn(listOf(fakeFeature))

        // Act
        val result = weatherRepository.getAlertsForLocation(testLocation)

        // Assert
        assertEquals(1, result.size)
        val alert = result.first()
        assertEquals("Test Area", alert.area)
        assertEquals("Type A", alert.awarenessType)
        assertEquals("Heavy Rainfall", alert.description)
        assertEquals("Rain Warning", alert.eventAwarenessName)
        assertEquals("Stay indoors", alert.instruction)
        assertEquals("Red", alert.riskMatrixColor)
        assertEquals("123-456", alert.contact)
    }
}