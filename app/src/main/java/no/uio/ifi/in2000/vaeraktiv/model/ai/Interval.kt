package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class Interval(
    val month: Int,
    val dayOfMonth: Int,
    val timeStart: String,
    val timeEnd: String,
    val activity: String,
    val activityDesc: String,
)