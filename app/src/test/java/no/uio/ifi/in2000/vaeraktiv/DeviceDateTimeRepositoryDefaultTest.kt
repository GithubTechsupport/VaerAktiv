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
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.test.filters.SdkSuppress
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeDataSource
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepositoryDefault
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@SdkSuppress(minSdkVersion = Build.VERSION_CODES.O)
class DeviceDateTimeRepositoryDefaultTest {

    private lateinit var dataSource: DeviceDateTimeDataSource
    private lateinit var repository: DeviceDateTimeRepositoryDefault

    @Before
    fun setup() {
        dataSource = mock(DeviceDateTimeDataSource::class.java)
        repository = DeviceDateTimeRepositoryDefault(dataSource)
    }

    @Test
    fun `getDateTime returns formatted date string`() {
        // Arrange
        val fakeDateTime = LocalDateTime.of(2025, 5, 3, 14, 0)
        `when`(dataSource.getCurrentLocalDateTime()).thenReturn(fakeDateTime)

        // Act
        val result = repository.getDateTime()

        // Assert
        assertEquals("2025-05-03", result)
    }

    @Test
    fun `getDateTime returns correct date for specific timezone`() {
        // Arrange: simulate Tokyo time (UTC+9), May 3, 2025 at 23:30
        val tokyoZone = ZoneId.of("Asia/Tokyo")
        val zonedDateTime = ZonedDateTime.of(2025, 5, 3, 23, 30, 0, 0, tokyoZone)
        val localDateTime = zonedDateTime.toLocalDateTime()

        `when`(dataSource.getCurrentLocalDateTime()).thenReturn(localDateTime)

        // Act
        val result = repository.getDateTime()

        // Assert
        assertEquals("2025-05-03", result)
    }
}