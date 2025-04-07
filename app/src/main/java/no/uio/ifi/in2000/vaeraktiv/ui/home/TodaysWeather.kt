package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.fonts.tungstenfontfamily
import no.uio.ifi.in2000.vaeraktiv.model.ui.ForecastToday
import no.uio.ifi.in2000.vaeraktiv.ui.theme.MainCard
import no.uio.ifi.in2000.vaeraktiv.ui.theme.SecondaryCard

@SuppressLint("DiscouragedApi")
@Composable
fun TodaysWeather(data: ForecastToday?) {
    val context = LocalContext.current
    val iconResId = context.resources.getIdentifier(data?.icon, "drawable", context.packageName) // skal bruke iconDescToday
    val cornerDp = 10.dp
    Spacer(modifier = Modifier.height(12.dp))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(MainCard, SecondaryCard)
                ),
                shape = RoundedCornerShape(cornerDp))
            .fillMaxWidth()
            .padding(15.dp)
    ) {
        /*
        Text(
            text ="Været i dag",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            fontFamily = tungstenfontfamily,
            letterSpacing = 2.sp
        )*/
        Spacer(modifier = Modifier.height(1.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ){
            Text(
                text = "Min temp",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 50.dp),
                fontFamily = tungstenfontfamily,
                fontSize = 14.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.weight(1.2f))
            Text(
                text = "Maks temp",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = tungstenfontfamily,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.weight(0.8f))
            Text(
                text = "Neste 6 timer",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(end = 50.dp),
                fontFamily = tungstenfontfamily,
                letterSpacing = 2.sp
            )
        }
        Spacer(modifier = Modifier.height(0.2.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically,
            //horizontalArrangement = Arrangement.SpaceEvenly,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                //.padding(8.dp)
        ) {
            Text(
                text = "${data?.tempMin}°",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 40.dp),
                fontFamily = tungstenfontfamily,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.weight(0.6f))
            Text(
                text = "${data?.tempMax}°",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                fontFamily = tungstenfontfamily,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.weight(0.42f))
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Dagens vær",
                modifier = Modifier.size(100.dp).padding(end = 40.dp)
            )
        }
    }
}