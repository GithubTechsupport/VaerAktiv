package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Properties

// Her kan vi bygge iconId på en bedre måte etter mvp
@Composable
fun DisplayWarning(warningInfo: Properties) {
    if (warningInfo.description != null || warningInfo.riskMatrixColor != null || warningInfo.eventAwarenessName != null) {
        val context = LocalContext.current
        val dangerColor = warningInfo.riskMatrixColor.orEmpty()
        val warningType = warningInfo.eventAwarenessName.orEmpty()
        val desc = warningInfo.description ?: "Ingen data"
        val iconResId =
            getWarningResId(context = context, warningType = warningType, dangerColor = dangerColor)

        val cornerDp = 10.dp
        Spacer(modifier = Modifier.height(12.dp))

        var isExpanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(
                    Color(0xFFFFD600),
                    shape = RoundedCornerShape(cornerDp)
                )
                .clickable { isExpanded = !isExpanded }
                .width(200.dp)
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
                    painter = painterResource(id = iconResId),
                    contentDescription = "Advarsel ikon",
                    modifier = Modifier.size(50.dp)
                )
                Text(
                    text = "Advarsel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
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
