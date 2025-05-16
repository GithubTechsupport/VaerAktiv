package no.uio.ifi.in2000.vaeraktiv.ui.home.weather

import android.util.Log
import java.time.DayOfWeek
import java.time.LocalDate


/**
 * Returns the day of the week for a given date string.
 *
 * @param date The date as a string.
 * @return The name of the day of the week. Returns "error" if parsing fails.
 */

fun getDayOfWeek(date: String): String {
    return try {
        val localDate = LocalDate.parse(date)
        when (localDate.dayOfWeek) {
            DayOfWeek.MONDAY -> "Mandag"
            DayOfWeek.TUESDAY -> "Tirsdag"
            DayOfWeek.WEDNESDAY -> "Onsdag"
            DayOfWeek.THURSDAY -> "Torsdag"
            DayOfWeek.FRIDAY -> "Fredag"
            DayOfWeek.SATURDAY -> "Lørdag"
            DayOfWeek.SUNDAY -> "Søndag"
            else -> "N/A"
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        "error"
    }
}