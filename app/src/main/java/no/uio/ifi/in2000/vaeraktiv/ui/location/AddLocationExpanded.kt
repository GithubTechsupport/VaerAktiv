package no.uio.ifi.in2000.vaeraktiv.ui.location

import SuggestionsOverlay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationExpanded(defaultPading: Dp, viewModel: FavoriteLocationViewModel) {
    var searchText by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    val predictions = viewModel.predictions.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Title row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(defaultPading)
        ) {
            Text(
                text = "Legg til sted",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Left,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.AddCircle,
                contentDescription = "Add Circle Icon",
                modifier = Modifier
                    .padding(defaultPading)
                    .size(50.dp)
            )
        }

        // Search input
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                viewModel.fetchPredictions(it)
            },
            placeholder = { Text("Søk") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = Modifier
                .padding(defaultPading)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (searchText.isNotBlank()) {
                    viewModel.addLocation(searchText)
                    searchText = ""
                    keyboardController?.hide()
                }
            })
        )

        // Suggestions list
        if (predictions.value.isNotEmpty()) {
            SuggestionsOverlay(
                predictions = predictions.value,
                onSuggestionClick = { prediction ->
                    val fullText = prediction.getFullText(null).toString()
                    val primaryText = prediction.getPrimaryText(null).toString()
                    viewModel.addLocation(fullText)
                    searchText = ""
                    viewModel.fetchPredictions("")
                    keyboardController?.hide()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .padding(horizontal = defaultPading)
            )
        }
    }
}
