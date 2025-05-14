package no.uio.ifi.in2000.vaeraktiv.ui.location


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R

@Composable
fun AddLocationExpanded(defaultPadding: Dp, viewModel: FavoriteLocationViewModel) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val predictions = viewModel.predictions.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Title row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(defaultPadding)
        ) {
            Text(
                text = stringResource(R.string.legg_til_sted),
                style = MaterialTheme.typography.titleLarge ,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .padding(defaultPadding)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add Place Icon",
                modifier = Modifier
                    .padding(defaultPadding)
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.background
            )
        }

        // Search input
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.fetchPredictions(it)
            },
            placeholder = { Text(stringResource(R.string.s_k)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .padding(defaultPadding)
                .fillMaxWidth()
        )

        if (predictions.value.isNotEmpty()) {
            SuggestionsOverlay(
                predictions = predictions.value,
                onSuggestionClick = { prediction ->
                    val fullText = prediction.getFullText(null).toString()
                    viewModel.addLocation(fullText)
                    searchText = ""
                    viewModel.fetchPredictions("")
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .padding(horizontal = defaultPadding)
            )
        }
    }
}
