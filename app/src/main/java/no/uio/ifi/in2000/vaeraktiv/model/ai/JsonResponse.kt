package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.Serializable

@Serializable
data class JsonResponse(
    val activities: List<Interval>
) {
    override fun toString(): String {
        val response = activities.joinToString("\n") {
            "${it.dayOfMonth}/${it.month} ${it.timeStart} - ${it.timeEnd}: ${it.activity}" // 23/9 12:00 - 13:00: Swimming
        }
        return response
    }
}