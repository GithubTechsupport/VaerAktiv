package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Properties

// Her kan vi bygge iconId på en bedre måte etter mvp
@Composable
fun DisplayWarning(data: List<Properties>) {
    if (data.isNotEmpty()) {
        var isExpanded by remember { mutableStateOf(false) }
        val cornerDp = 10.dp
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .background(
                    Color(0xFFFFD600),
                    shape = RoundedCornerShape(cornerDp)
                )
                .clickable { isExpanded = !isExpanded }
                .width(300.dp)
                .padding(4.dp)
                .animateContentSize(), // Animasjon for størrelse
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_warning_generic_yellow),
                    contentDescription = "Advarsel ikon",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Advarsel",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }


            if (isExpanded) {
                Box(modifier = Modifier
                    .width(200.dp)
                    .padding(top = 4.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.onBackground)
                )
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    data.forEach { warning ->
                        val iconType = warning.awareness_type?.split("; ")?.getOrNull(1) ?: "generic"
                        val doubleIconCheck = iconType.split("-").joinToString("") { it.trim() }
                        if (warning.description != null || warning.riskMatrixColor != null || doubleIconCheck != "generic") {
                            val dangerColor = warning.riskMatrixColor.orEmpty()
                            val desc = warning.description ?: "Ingen data"
                            val instruct = warning.instruction ?: "Ingen data"
                            val type = warning.eventAwarenessName ?: "Ingen data"
                            val iconResId = getWarningResId(context = context, warningType = doubleIconCheck, dangerColor = dangerColor)

                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = iconResId),
                                        contentDescription = "Advarsel ikon",
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Spacer(modifier = Modifier.width(30.dp))
                                    Column (

                                    ) {
                                        Text(
                                            text = type,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = instruct,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Text(
                        text = "Kontaktinformasjon: ${data[0].contact ?: "Ikke funnet"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
private fun getWarningResId(context: android.content.Context, warningType: String, dangerColor: String): Int {
    val iconString = "icon_warning_${warningType.lowercase()}_${dangerColor.lowercase()}"
    val resId = context.resources.getIdentifier(iconString, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.icon_warning_generic_yellow
}
