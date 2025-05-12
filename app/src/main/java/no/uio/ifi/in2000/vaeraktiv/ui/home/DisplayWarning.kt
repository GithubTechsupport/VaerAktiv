package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData

@Composable
fun DisplayWarning(data: List<AlertData>) {
    if (data.isNotEmpty()) {
        var isExpanded by remember { mutableStateOf(false) }
        val cornerDp = 10.dp
        val context = LocalContext.current
        val contactInfo = data.first().contact ?: "N/A"

        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(cornerDp)
                )
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(cornerDp))
                .clickable { isExpanded = !isExpanded }
                .fillMaxWidth()
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_warning_generic_yellow),
                    contentDescription = "Warning icon",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Advarsel",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                )
            }

            if (isExpanded) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(color = MaterialTheme.colorScheme.onPrimary)
                )
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    data.forEach { warning ->
                        val iconType = warning.awarenessType?.split("; ")?.getOrNull(1) ?: "generic"
                        if (warning.description != null || warning.riskMatrixColor != null || iconType != "generic") {
                            val dangerColor = warning.riskMatrixColor.orEmpty()
                            val instruct = warning.instruction ?: "N/A"
                            val type = warning.eventAwarenessName ?: "Ingen data"
                            val iconResId = getWarningResId(context = context, warningType = iconType, dangerColor = dangerColor)

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 12.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = iconResId),
                                    contentDescription = "Warning icon: $type, $dangerColor",
                                    modifier = Modifier.size(60.dp)
                                )
                                Spacer(modifier = Modifier.width(30.dp))
                                Column {
                                    Text(
                                        text = type,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "Instruksjoner: $instruct",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Start
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    Text(
                        text = "Kontaktinformasjon: $contactInfo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
private fun getWarningResId(context: android.content.Context, warningType: String, dangerColor: String): Int {
    val basePrefix = "icon_warning_"
    val parts = warningType.split("-").filter { it.isNotEmpty() }
    val noColorIcons = setOf("extreme")

    fun tryIcon(type: String): Int {
        val combinedLower = type.lowercase()
        val iconName = if (combinedLower in noColorIcons) {
            (basePrefix + combinedLower)
        } else {
            (basePrefix + combinedLower + "_" + dangerColor.lowercase())
        }
        return context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    val combined = parts.joinToString("") { it.trim() }
    tryIcon(combined).takeIf { it != 0 }?.let {return it}
    tryIcon(parts.getOrNull(0) ?: "generic").takeIf { it != 0 }?.let { return it }
    tryIcon(parts.getOrNull(1) ?: "generic").takeIf { it != 0 }?.let { return it }

    return R.drawable.icon_warning_generic_yellow
}