package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.util.Log
import java.time.LocalDate


/**
 * Returns the day of the week for a given date string.
 *
 * @param date The date as a string.
 * @return The name of the day of the week. Returns "error" if parsing fails.
 */
@RequiresApi(Build.VERSION_CODES.O)

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