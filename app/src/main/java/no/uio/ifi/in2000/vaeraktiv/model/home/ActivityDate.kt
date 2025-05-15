package no.uio.ifi.in2000.vaeraktiv.model.home

import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion

/**
 * Groups activity suggestions by a specific date.
 *
 * @param date the ISO-formatted date (e.g., "2023-07-21")
 * @param activities list of AI-generated or place-based activities for that date
 */
data class ActivityDate(
    val date: String,
    val activities: List<ActivitySuggestion>
)
