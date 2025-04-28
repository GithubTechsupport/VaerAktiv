package no.uio.ifi.in2000.vaeraktiv.ui.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.model.preferences.Preference


@Composable
fun ActivityScreen(isOnline: Boolean, viewModel: ActivityScreenViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        item{
            Text(
                text = "Instillinger",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(10.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        item {
            Text(
                text = "Hvilke aktiviteter liker du?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp),
            )
            Activity(Preference("Ballsport", false))
            Activity(Preference("Håndtverk", false))
            Activity(Preference("Friluftsliv", false))
            Activity(Preference("Gå turer", false))
            Activity(Preference("Trening", false))


            Text(
                text = "Liker du å være sosial, eller gjøre ting alene?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp),
            )
            Activity(Preference("Sammen med andre", false))
            Activity(Preference("Alene", false))
        }

        item {
            Text(
                text = "Vil du bli tilbydt aktiviteter som koster penger?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(10.dp),
            )
            Activity(Preference("Ja", false))
        }
    }
}