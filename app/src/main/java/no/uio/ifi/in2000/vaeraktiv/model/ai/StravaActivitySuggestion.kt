package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response for a route suggestion, currently only Strava routes are supported.
 *
 * @property month The month of the suggested activity.
 * @property dayOfMonth The day of the month of the suggested activity.
 * @property timeStart The start time of the suggested activity.
 * @property timeEnd The end time of the suggested activity.
 * @property activityName The name of the suggested activity.
 * @property activityDesc A description of the suggested activity.
 * @property id The unique identifier for the activity suggestion.
 * @property routeName The name of the suggested route.
 * @property distance The distance of the suggested route in meters.
 * @property polyline The encoded polyline representation of the suggested route.
 */
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
