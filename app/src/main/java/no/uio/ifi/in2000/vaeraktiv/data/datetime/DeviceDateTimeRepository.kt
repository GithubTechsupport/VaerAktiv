package no.uio.ifi.in2000.vaeraktiv.data.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class DeviceDateTimeRepository @Inject constructor(private val deviceDateTimeDataSource : DeviceDateTimeDataSource) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = deviceDateTimeDataSource.getCurrentLocalDateTime().format(formatter)
        return formattedDateTime
    }
}