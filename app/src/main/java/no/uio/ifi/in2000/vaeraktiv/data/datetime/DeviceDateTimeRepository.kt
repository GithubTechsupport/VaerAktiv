package no.uio.ifi.in2000.vaeraktiv.data.datetime

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.format.DateTimeFormatter
import javax.inject.Inject


interface DeviceDateTimeRepository {

    fun getDateTime(): String
}