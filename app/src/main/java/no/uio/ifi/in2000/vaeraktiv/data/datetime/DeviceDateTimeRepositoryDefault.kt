package no.uio.ifi.in2000.vaeraktiv.data.datetime

import java.time.format.DateTimeFormatter
import javax.inject.Inject


class DeviceDateTimeRepositoryDefault @Inject constructor(private val deviceDateTimeDataSource : DeviceDateTimeDataSource)
    : DeviceDateTimeRepository
{
    override fun getDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = deviceDateTimeDataSource.getCurrentLocalDateTime().format(formatter)
        return formattedDateTime
    }
}