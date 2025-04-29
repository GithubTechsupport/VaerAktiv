package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun DisplayWeather(data: ForecastToday?, uiState: HomeScreenUiState) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data?.iconNow, "drawable", context.packageName)
    val cornerDp = 10.dp
    var expanded by remember { mutableStateOf(false) }
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        // First row: weather icon and current temperature
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon cell
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Ikon",
                    modifier = Modifier.size(125.dp)
                )
            }
            // Temperature cell
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${data?.tempNow}°",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }
        }
        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            // Second row: UV, precipitation, and wind speed details in a background box
            Column(
                modifier = Modifier
//                    .background(
//                        color = MaterialTheme.colorScheme.primaryContainer,
//                        shape = RoundedCornerShape(cornerDp)
//                    )
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp) // Use the same overall padding as above
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // UV details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${data?.uv} UV",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            //fontFamily = tungstenfontfamily,
                            fontSize = 20.sp,
                            //letterSpacing = 2.sp
                        )
                    }
                    // Precipitation details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${data?.precipitationAmount} mm",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            //fontFamily = tungstenfontfamily,
                            fontSize = 20.sp,
                            //letterSpacing = 2.sp
                        )
                    }
                    // Wind speed details
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${data?.windSpeed} m/s",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            //fontFamily = tungstenfontfamily,
                            fontSize = 20.sp,
                            //letterSpacing = 2.sp
                        )
                    }
                }
                if (uiState.next24HoursError != null) {
                    ErrorMessage(
                        message = "Error fetching today's weather: ${uiState.next24HoursError}"
                    )
                } else {
                    Row {
                        DisplayHourlyForecast(uiState.next24Hours)
                    }
                }
                if (uiState.sunRiseSetError != null) {
                    ErrorMessage(
                        message = "Error fetching sunrise/sunset data: ${uiState.sunRiseSetError}"
                    )
                } else {
                    SunRiseSet(uiState.sunRiseSet)
                }
            }
        }
    }
}
