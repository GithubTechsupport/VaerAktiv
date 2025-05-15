package no.uio.ifi.in2000.vaeraktiv.data.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Provides the current local date and time based on the system default timezone.
 */
class DeviceDateTimeDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId = ZoneId.systemDefault() // system timezone ID

    /** Returns the current LocalDateTime in the system default timezone. */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentLocalDateTime(): LocalDateTime {
        val datetime = LocalDateTime.now(zoneId)
        return datetime
    }

}
