package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ActivitySuggestion {
    val month: Int
    val dayOfMonth: Int
    val timeStart: String
    val timeEnd: String
    val activityName: String
    val activityDesc: String
}

@Serializable
@SerialName("PlaceActivitySuggestion")
data class PlacesActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,

    val id: String,
    val placeName: String,
    val formattedAddress: String,
    val coordinates: Pair<Double, Double>,
) : ActivitySuggestion

@Serializable
@SerialName("StravaActivitySuggestion")
data class StravaActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,

    val id: String,
    val routeName: String,
    val distance: Double,
    val polyline: String,
) : ActivitySuggestion

@Serializable
@SerialName("CustomActivitySuggestion")
data class CustomActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,
) : ActivitySuggestion