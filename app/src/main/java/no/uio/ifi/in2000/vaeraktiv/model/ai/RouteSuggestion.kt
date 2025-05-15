package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

/**
 * Data class representing a route suggestion which is included in the prompt.
 *
 * @property id The unique identifier for the route suggestion.
 * @property routeName The name of the suggested route.
 * @property distance The distance of the route in meters.
 * @property elevationGain The total elevation gain of the route in meters (nullable).
 * @property averageGrade The average grade of the route as a percentage (nullable).
 * @property polyline The encoded polyline representation of the route.
 * @property startPosition The starting position of the route as a pair of latitude and longitude (nullable).
 * @property endPosition The ending position of the route as a pair of latitude and longitude (nullable).
 * @property source The API source from which the route suggestion was obtained.
 */
@Serializable
data class RouteSuggestion(
    val id: String,
    val routeName: String,
    val distance: Double,
    val elevationGain: Double?,
    val averageGrade: Double?,
    val polyline: String,
    val startPosition: Pair<Double, Double>?,
    val endPosition: Pair<Double, Double>?,
    val source: RouteSource
)
