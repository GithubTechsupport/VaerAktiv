package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData

@Composable
fun WarningDetailItem(warning: AlertData) {
    val iconType = warning.awarenessType?.split("; ")?.getOrNull(1) ?: stringResource(R.string.generic)
    val dangerColor = warning.riskMatrixColor.orEmpty()
    val instruct = warning.instruction ?: stringResource(R.string.n_a)
    val type = warning.eventAwarenessName ?: stringResource(R.string.ingen_data)

    if (warning.description != null || !warning.riskMatrixColor.isNullOrEmpty() || iconType != stringResource(R.string.generic)) {
        val iconResId = getWarningResId(
            context = LocalContext.current,
            warningType = iconType,
            dangerColor = dangerColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "Warning icon: $type, $dangerColor",
                modifier = Modifier.size(60.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
                Text(
                    text = stringResource(R.string.instruksjoner, instruct),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
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