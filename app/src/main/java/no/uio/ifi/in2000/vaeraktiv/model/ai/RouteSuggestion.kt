package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

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
