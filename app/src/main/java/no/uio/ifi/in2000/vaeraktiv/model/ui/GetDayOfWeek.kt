package no.uio.ifi.in2000.vaeraktiv.model.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun getDayOfWeek(date: String): String {
    return try {
        val localDate = LocalDate.parse(date)
        val dayOfWeek = localDate.dayOfWeek
        when (dayOfWeek!!) {
            java.time.DayOfWeek.MONDAY -> "Mandag"
            java.time.DayOfWeek.TUESDAY -> "Tirsdag"
            java.time.DayOfWeek.WEDNESDAY -> "Onsdag"
            java.time.DayOfWeek.THURSDAY -> "Torsdag"
            java.time.DayOfWeek.FRIDAY -> "Fredag"
            java.time.DayOfWeek.SATURDAY -> "Lørdag"
            java.time.DayOfWeek.SUNDAY -> "Søndag"
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        "error"
    }
}