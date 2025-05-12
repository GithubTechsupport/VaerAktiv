package no.uio.ifi.in2000.vaeraktiv.model.home

import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion

data class ActivityDate(
    val date: String,
    val activities: List<ActivitySuggestion>
)
