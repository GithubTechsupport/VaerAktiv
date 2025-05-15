package no.uio.ifi.in2000.vaeraktiv.data.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.ZoneId

class DeviceDateTimeDataSource {
    @RequiresApi(Build.VERSION_CODES.O)
    private val zoneId = ZoneId.systemDefault() //Get the timezone id of the system timezone.

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentLocalDateTime(): LocalDateTime {
        val datetime = LocalDateTime.now(zoneId)
        return datetime
    }

}