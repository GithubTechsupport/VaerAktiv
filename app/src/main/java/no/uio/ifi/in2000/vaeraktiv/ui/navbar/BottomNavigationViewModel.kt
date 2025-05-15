package no.uio.ifi.in2000.vaeraktiv.ui.navbar

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.vaeraktiv.R

data class NavItem(val route: String, val iconId: Int)

/**
 * ViewModel for bottom navigation bar state and actions.
 */
class BottomNavigationViewModel : ViewModel() {
    private val _navItems = listOf(
        NavItem("location", R.drawable.location),
        NavItem("home", R.drawable.sun),
        NavItem("map", R.drawable.map)
    )
    val navItems: List<NavItem> = _navItems

    private val _selectedRoute = mutableStateOf("home")
    val selectedRoute: State<String> = _selectedRoute

    /**
     * Handles navigation item selection and performs navigation.
     */
    fun onNavItemClick(navController: NavController, route: String) {
        if (_selectedRoute.value == route) return
        _selectedRoute.value = route
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId)
            launchSingleTop = true
        }
    }

    /**
     * Updates the selected route if it exists in the nav items.
     */
    fun updateSelectedRoute(route: String) {
        if (_navItems.any { it.route == route }) {
            _selectedRoute.value = route
        }
    }
}
