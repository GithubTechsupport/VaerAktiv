package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

/**
 * Interface representing an activity suggestion.
 * To serve as an interface for responses of different types by the model.
 *
 * @property month The month of the suggested activity.
 * @property dayOfMonth The day of the month of the suggested activity.
 * @property timeStart The start time of the suggested activity.
 * @property timeEnd The end time of the suggested activity.
 * @property activityName The name of the suggested activity.
 * @property activityDesc A description of the suggested activity.
 */
@Serializable
sealed interface ActivitySuggestion {
    val month: Int
    val dayOfMonth: Int
    val timeStart: String
    val timeEnd: String
    val activityName: String
    val activityDesc: String
}
