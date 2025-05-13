package no.uio.ifi.in2000.vaeraktiv.data.datetime


fun interface DeviceDateTimeRepository {
    fun getDateTime(): String
}