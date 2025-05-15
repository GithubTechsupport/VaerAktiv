package no.uio.ifi.in2000.vaeraktiv.data.datetime

import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Default implementation of DeviceDateTimeRepository that formats dates.
 */
class DeviceDateTimeRepositoryDefault @Inject constructor(
    private val deviceDateTimeDataSource: DeviceDateTimeDataSource
) : DeviceDateTimeRepository {

    /** Returns current date as a string in 'yyyy-MM-dd' format. */
    override fun getDateTime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDateTime = deviceDateTimeDataSource.getCurrentLocalDateTime().format(formatter)
        return formattedDateTime
    }
}