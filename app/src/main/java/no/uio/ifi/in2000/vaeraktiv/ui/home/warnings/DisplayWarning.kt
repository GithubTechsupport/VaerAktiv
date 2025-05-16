package no.uio.ifi.in2000.vaeraktiv.ui.home.warnings

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData

/**
 * Displays a warning if data exists, with the option to expand the content.
 *
 * @param data List of alert data.
 */
@Composable
fun DisplayWarning(data: List<AlertData>) {
    if (data.isNotEmpty()) {
        var isExpanded by remember { mutableStateOf(false) }
        val cornerDp = 10.dp
        val contactInfo = data.first().contact ?: stringResource(R.string.n_a)

        Column(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(cornerDp)
                )
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(cornerDp))
                .clickable { isExpanded = !isExpanded }
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WarningHeader()
            if (isExpanded) {
                ExpandedWarningContent(data, contactInfo)
            }
        }
    }
}

/** Displays the warning header with icon and text. */
@Composable
private fun WarningHeader() {
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
            text = stringResource(R.string.advarsel),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Displays detailed content for warnings, including contact information.
 *
 * @param warnings List of alerts.
 * @param contactInfo Contact information as text.
 */
@Composable
private fun ExpandedWarningContent(warnings: List<AlertData>, contactInfo: String) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.onBackground)
        )

        warnings.forEach { warning ->
            WarningDetailItem(warning = warning)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.kontaktinformasjon, contactInfo),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}