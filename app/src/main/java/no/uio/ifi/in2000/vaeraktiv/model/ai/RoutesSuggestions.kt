package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class RoutesSuggestions(
    val suggestions: List<RouteSuggestion>
) {
    override fun toString(): String {
        return suggestions.joinToString(separator = "\n"){
            "${it.routeName} - ${it.distance} meters | ${it.polyline}"
        }
    }
}

@Serializable
data class RouteSuggestion(
    val id: String,                  // Strava segment ID or generated Overpass UUID
    val routeName: String,
    val distance: Double,            // meters
    val elevationGain: Double?,      // meters (nullable for flat paths)
    val averageGrade: Double?,       // percent (nullable)
    val polyline: String,            // encoded or generated
    val startPosition: Pair<Double, Double>?, // (lat, lon)
    val endPosition: Pair<Double, Double>?,   // (lat, lon)
    val source: RouteSource          // STRAVA or OVERPASS
)

enum class RouteSource { STRAVA, OVERPASS }
