package no.uio.ifi.in2000.vaeraktiv.model.navbar

data class NavbarUiState(
    val isOnline: Boolean = true,
    val showNoNetworkDialog: Boolean = false,
    val isLoading: Boolean = false,
    val selectedRoute: String = "home",
    val isOnboardingRoute: Boolean = false
)