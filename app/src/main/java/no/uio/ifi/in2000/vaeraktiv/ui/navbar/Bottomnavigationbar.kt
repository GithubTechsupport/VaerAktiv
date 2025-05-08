package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun BottomNavigationBar(navController: NavController, getSelectedRoute: () -> String, setSelectedRoute: (String) -> Unit) {
    val navItems = listOf(
        "location" to R.drawable.location,
        "home" to R.drawable.sun,
        "map" to R.drawable.map
    )

    BottomAppBar(
        modifier = Modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background,
    ) {
        navItems.forEach { (route, iconId) ->
            val selected = getSelectedRoute() == route
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(4.dp)
                    .background(
                        color = if (selected) MaterialTheme.colorScheme.background else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                        )
                    .clickable {
                        if (selected) return@clickable
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                        setSelectedRoute(route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = painterResource(id = iconId),
                        contentDescription = route,
                        tint = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}