package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday

@SuppressLint("DiscouragedApi")
@Composable
fun TodaysWeather(data: ForecastToday?) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data?.icon, "drawable", context.packageName)
    val cornerDp = 10.dp

    val labelFontSize = 20.sp
    val valueFontSize = 30.sp

    Spacer(modifier = Modifier.height(12.dp))

}
