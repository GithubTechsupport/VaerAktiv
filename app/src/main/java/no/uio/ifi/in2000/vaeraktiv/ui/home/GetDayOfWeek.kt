package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
import java.time.LocalDate

fun getDayOfWeek(date: String): String {
    return try {
        val localDate = LocalDate.parse(date)
        when (localDate.dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> "Mandag"
            java.time.DayOfWeek.TUESDAY -> "Tirsdag"
            java.time.DayOfWeek.WEDNESDAY -> "Onsdag"
            java.time.DayOfWeek.THURSDAY -> "Torsdag"
            java.time.DayOfWeek.FRIDAY -> "Fredag"
            java.time.DayOfWeek.SATURDAY -> "Lørdag"
            java.time.DayOfWeek.SUNDAY -> "Søndag"
            else -> "N/A"
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        "error"
    }
}