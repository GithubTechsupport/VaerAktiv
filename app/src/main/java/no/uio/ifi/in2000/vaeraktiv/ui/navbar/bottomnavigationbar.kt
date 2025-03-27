package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.R

/*
* This is a navigation bar for the application, this function is called in the MainActivity.kt.
* When this function is called a display in the button wil be called, this display will have tre buttons.
* One for activity, one for home and one for location.
* I have used a list and a forEach loop to create the buttons. The rest of the code is just styling, on how the buttons should look like.
* */
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navItems = listOf(
        "activity" to R.drawable.walk,
        "home" to R.drawable.sun,
        "location" to R.drawable.location
    )

    var selectedRoute by remember { mutableStateOf("home") }

    BottomNavigation(
        modifier = Modifier.height(80.dp),
        backgroundColor = Color(0xFFBCDEFD)
    ) {
        navItems.forEach { (route, iconId) ->
            val selected = selectedRoute == route
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(8.dp)
                    .background(
                        color = if (selected) Color(0xFF6BAEDF) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        selectedRoute = route
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = route
                    )
                }
            }
        }
    }
}