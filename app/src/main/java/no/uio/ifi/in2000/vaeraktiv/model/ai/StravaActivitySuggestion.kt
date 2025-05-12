package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
