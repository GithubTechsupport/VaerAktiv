package no.uio.ifi.in2000.vaeraktiv.data

enum class ErrorMessages(val message: String) {
    NO_FORECAST_FOUND("No forecast found"),
    NETWORK_ERROR("Network error"),
    UNKNOWN_ERROR("An unknown error occurred"),
    LOCATION_ERROR("Location error"),
    NO_LOCATION_PERMISSION("No location permission"),
    INVALID_INPUT("Invalid input provided")
}