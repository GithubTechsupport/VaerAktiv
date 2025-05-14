package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(
    navController: NavController,
    viewModel: BottomNavigationViewModel
) {
    BottomAppBar(
        modifier = Modifier.height(80.dp),
        containerColor = MaterialTheme.colorScheme.onBackground,
        contentColor = MaterialTheme.colorScheme.background,
    ) {
        viewModel.navItems.forEach { navItem ->
            val selected = viewModel.selectedRoute.value == navItem.route
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
                        viewModel.onNavItemClick(navController, navItem.route)
                    },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = painterResource(id = navItem.iconId),
                        contentDescription = navItem.route,
                        tint = if (selected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
                    )
                }
            }
        }
    }
}