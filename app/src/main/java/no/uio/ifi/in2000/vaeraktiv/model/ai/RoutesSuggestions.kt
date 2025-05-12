package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class RoutesSuggestions(
    val suggestions: List<RouteSuggestion>
) {
    override fun toString(): String {
        return suggestions.joinToString(separator = "\n\n"){
            """
            Route name: ${it.routeName}
            Route distance: ${it.distance} meters
            Route polyline: ${it.polyline}
            """.trimIndent()
        }
    }
}