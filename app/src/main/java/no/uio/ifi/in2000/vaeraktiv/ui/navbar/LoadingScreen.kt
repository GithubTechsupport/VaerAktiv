package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import no.uio.ifi.in2000.vaeraktiv.R

/*
* This function is a loadingscreen. This function is called in Navbar.kt. When this function is called a loading screen will be displayed with a sun and a sky image.
* */
@Preview(showBackground = true)
@Composable
fun LoadingScreen() {
    val loadingImages = listOf(
        R.drawable.loading3,
        R.drawable.loading2,
        R.drawable.loading1
    )
    var currentImageIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500) // Delay for 1 second
            currentImageIndex = (currentImageIndex + 1) % loadingImages.size
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = loadingImages[currentImageIndex]),
            contentDescription = "Loading",
            modifier = Modifier.scale(0.7f)
        )
    }
}
