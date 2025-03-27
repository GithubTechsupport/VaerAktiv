package no.uio.ifi.in2000.vaeraktiv.data.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime


class DeviceDateTimeRepository {
    private val deviceDateTimeDataSource= DeviceDateTimeDataSource()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateTime(): LocalDateTime? {
        return deviceDateTimeDataSource.getCurrentLocalDateTime()
    }
}