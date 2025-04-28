package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

interface ActivitySuggestion {
    val month: Int
    val dayOfMonth: Int
    val timeStart: String
    val timeEnd: String
    val activityName: String
    val activityDesc: String
    val source: ActivitySource
}

@Serializable
data class PlacesActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,
    override val source: ActivitySource = ActivitySource.PLACES,

    val id: String,
    val placeName: String,
    val formattedAddress: String,
    val coordinates: Pair<Double, Double>,
) : ActivitySuggestion

@Serializable
data class StravaActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,
    override val source: ActivitySource = ActivitySource.STRAVA,

    val id: String,
    val routeName: String,
    val distance: Double,
    val polyline: String,
) : ActivitySuggestion

@Serializable
data class CustomActivitySuggestion (
    override val month: Int,
    override val dayOfMonth: Int,
    override val timeStart: String,
    override val timeEnd: String,
    override val activityName: String,
    override val activityDesc: String,
    override val source: ActivitySource = ActivitySource.CUSTOM,
) : ActivitySuggestion

enum class ActivitySource {
    STRAVA,
    OVERPASS,
    PLACES,
    CUSTOM,
}