package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a AI response for a suggestion for an activity at a specific place.
 * Currently only from Google Places.
 *
 * @property month The month of the activity suggestion.
 * @property dayOfMonth The day of the month of the activity suggestion.
 * @property timeStart The start time of the activity suggestion.
 * @property timeEnd The end time of the activity suggestion.
 * @property activityName The name of the suggested activity.
 * @property activityDesc A description of the suggested activity.
 * @property id The unique identifier for the place.
 * @property placeName The name of the place.
 * @property formattedAddress The formatted address of the place.
 * @property coordinates The geographical coordinates (latitude, longitude) of the place.
 */
@Serializable
@SerialName("PlaceActivitySuggestion")
data class PlaceActivitySuggestion (
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
