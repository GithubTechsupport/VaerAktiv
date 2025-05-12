package no.uio.ifi.in2000.vaeraktiv.model.home

data class AlertData(
    val area: String,
    val awarenessType: String? = null,
    val description: String? = null,
    val eventAwarenessName: String? = null,
    val instruction: String? = null,
    val riskMatrixColor: String? = null,
    val contact : String? = null
)
