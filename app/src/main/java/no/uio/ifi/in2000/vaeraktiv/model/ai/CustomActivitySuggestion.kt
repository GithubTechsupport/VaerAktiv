package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data class representing a response from the model.
 * This activity is completely custom and is not based on any data from the APIs.
 *
 * @property month The month of the activity suggestion.
 * @property dayOfMonth The day of the month of the activity suggestion.
 * @property timeStart The start time of the activity suggestion.
 * @property timeEnd The end time of the activity suggestion.
 * @property activityName The name of the suggested activity.
 * @property activityDesc A description of the suggested activity.
 */
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
