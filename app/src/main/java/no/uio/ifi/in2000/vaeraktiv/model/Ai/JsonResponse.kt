package no.uio.ifi.in2000.vaeraktiv.model.Ai

data class JsonResponse(
    val intervals: List<Interval>
) {
    override fun toString(): String {
        val response = intervals.joinToString("\n") {
            "${it.dayOfMonth}/${it.month} ${it.timeStart} - ${it.timeEnd}: ${it.activity}" // 23/9 12:00 - 13:00: Swimming
        }
        return response
    }
}