package no.uio.ifi.in2000.vaeraktiv


import android.os.Build
import androidx.test.filters.SdkSuppress
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeDataSource
import no.uio.ifi.in2000.vaeraktiv.data.datetime.DeviceDateTimeRepositoryDefault
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

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