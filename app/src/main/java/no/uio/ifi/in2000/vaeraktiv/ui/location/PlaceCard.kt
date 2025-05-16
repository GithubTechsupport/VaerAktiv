package no.uio.ifi.in2000.vaeraktiv.ui.location

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.vaeraktiv.R
import no.uio.ifi.in2000.vaeraktiv.model.aggregateModels.Location
import no.uio.ifi.in2000.vaeraktiv.model.home.FavoriteLocation
import no.uio.ifi.in2000.vaeraktiv.utils.IconMapper

/**
 * The card template for all the weather information to be filled into.
 * @param FavoriteLocation The dataclass which has the weather information for the specific location
 */

@Composable
fun PlaceCard(
    location: FavoriteLocation,
    defaultPadding: Dp,
    viewModel: FavoriteLocationViewModel,
) {
    val resourceId = IconMapper.fromName(location.iconDesc)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(defaultPadding)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = MaterialTheme.colorScheme.onBackground,
            )
            .clickable(
                onClick = {
                    viewModel.updateCurrentLocation(
                        Location(location.name, location.lat, location.lon)
                    )
                }
            )
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    horizontal = 8.dp,
                    vertical = defaultPadding)
        ) {
            Text(
                text = location.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .padding(defaultPadding)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            viewModel.deleteLocation(location.name)
                            viewModel.getData()
                        }
                    )
            ) {
                // This icon wil delete the PlaceCard
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete Icon",
                    modifier = Modifier
                        .padding(defaultPadding)
                        .size(48.dp),
                    tint = MaterialTheme.colorScheme.background
                )
            }

        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = defaultPadding)
            .height(1.dp)
            .background(MaterialTheme.colorScheme.background)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                Image(
                    painter = painterResource(id = resourceId),
                    contentDescription = location.iconDesc,
                    modifier = Modifier
                        .padding(defaultPadding)
                )
            }
            Column (
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            ){
                // Short description, UV, highest and lowest temp
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = location.shortDesc,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = stringResource(R.string.uv, location.uv),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = stringResource(
                            R.string.hoghest_lowest_temp,
                            location.highestTemp,
                            location.lowestTemp
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Wind and downPour
                    Text(
                        text = stringResource(R.string.m_s, location.wind),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                    Text(
                        text = stringResource(R.string.nedb_r_ml, location.downPour),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .padding(defaultPadding)
                    )
                }
            }
        }
    }
}