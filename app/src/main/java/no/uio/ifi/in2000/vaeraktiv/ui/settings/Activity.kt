package no.uio.ifi.in2000.vaeraktiv.ui.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference

@Composable
fun Activity(
    activity: Preference,
    viewModel: PreferencesViewModel
) {
    val darkGreen = Color(0xFF2F8166)

    Button(
        onClick = {
            viewModel.onPreferenceToggled(activity, !activity.isEnabled)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (activity.isEnabled) darkGreen else Color.White,
            contentColor = if (activity.isEnabled) Color.White else darkGreen
        ),
        border = if(!activity.isEnabled) BorderStroke(2.dp, darkGreen) else null,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp)
    ) {
        if (activity.isEnabled) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White, // White icon when enabled
                modifier = Modifier.padding(end = 8.dp)
            )
        }
        Text(
            text = activity.desc,
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Start,
                color = if (activity.isEnabled) Color.White else darkGreen
            ),
            modifier = Modifier.weight(1f)
        )
    }
}