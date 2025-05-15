package no.uio.ifi.in2000.vaeraktiv.data.datetime

import java.time.LocalDateTime
import java.time.ZoneId

class DeviceDateTimeDataSource {
    private val zoneId = ZoneId.systemDefault() //Get the timezone id of the system timezone.

    fun getCurrentLocalDateTime(): LocalDateTime {
        val datetime = LocalDateTime.now(zoneId)
        return datetime
    }

}