package no.uio.ifi.in2000.vaeraktiv.data.datetime

import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Provides the current local date and time based on the system default timezone.
 */
class DeviceDateTimeDataSource {
    private val zoneId = ZoneId.systemDefault() //Get the timezone id of the system timezone.

    /** Returns the current LocalDateTime in the system default timezone. */
    fun getCurrentLocalDateTime(): LocalDateTime {
        val datetime = LocalDateTime.now(zoneId)
        return datetime
    }

}