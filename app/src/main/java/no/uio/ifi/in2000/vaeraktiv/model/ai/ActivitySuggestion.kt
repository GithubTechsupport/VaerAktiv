package no.uio.ifi.in2000.vaeraktiv.model.ai

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
