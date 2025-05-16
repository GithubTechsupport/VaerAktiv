package no.uio.ifi.in2000.vaeraktiv.ui.home.weather

import android.content.Context
import android.util.Log
import no.uio.ifi.in2000.vaeraktiv.R
import java.time.DayOfWeek
import java.time.LocalDate


/**
 * Returns the day of the week for a given date string.
 *
 * @param date The date as a string.
 * @return The name of the day of the week. Returns "error" if parsing fails.
 */
fun getDayOfWeek(date: String, context: Context): String {
    return try {
        val localDate = LocalDate.parse(date)
        val dayOfWeek = localDate.dayOfWeek
        if (dayOfWeek == null) {
            context.getString(R.string.n_a)
        } else {
            context.getString(
                when (dayOfWeek) {
                    DayOfWeek.MONDAY -> R.string.mandag
                    DayOfWeek.TUESDAY -> R.string.tirsdag
                    DayOfWeek.WEDNESDAY -> R.string.onsdag
                    DayOfWeek.THURSDAY -> R.string.torsdag
                    DayOfWeek.FRIDAY -> R.string.fredag
                    DayOfWeek.SATURDAY -> R.string.lordag
                    DayOfWeek.SUNDAY -> R.string.sondag
                }
            )
        }
    } catch (e: Exception) {
        Log.e("WeatherWeek", "Error getting day of week: ", e)
        context.getString(R.string.error)
    }
}