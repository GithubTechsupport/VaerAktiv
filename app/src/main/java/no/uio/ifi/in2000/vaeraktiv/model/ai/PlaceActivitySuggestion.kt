package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * AI-suggested activity tied to a specific place, with address info.
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
