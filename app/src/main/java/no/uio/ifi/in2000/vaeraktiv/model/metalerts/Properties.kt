package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    @SerialName("altitude_above_sea_level")
    val altitudeAboveSeaLevel: Int? = null,
    val area: String? = null, // må ha med
    val awarenessResponse: String? = null,
    val awarenessSeriousness: String? = null,
    @SerialName("awareness_level")
    val awarenessLevel: String? = null, // burde ha med (gir alvorlighetsgraden det varsles om)
    @SerialName("awareness_type")
    val awarenessType: String? = null, // kan ha med (gir hva det varsles om)
    @SerialName("ceiling_above_sea_level")
    val ceilingAboveSeaLevel: Int? = null,
    val certainty: String? = null, // kanskje (gir sannynligheten for hendelsen)
    val consequences: String? = null, // mulig å ha med (gir konsekvenser av situasjonen / beskrivelse)
    val contact: String? = null,
    val county: List<Int>? = null, // Tom liste i JSON, kan være Int eller String avhengig av API
    val description: String? = null, // gir en veldig detaljert beskrivelse, kan være litt lang
    val event: String? = null,
    val eventAwarenessName: String? = null, // Lurt å ha med. Gir en kort og presis beskrivelse av varselet
    val geographicDomain: String? = null,
    val id: String? = null,
    val instruction: String? = null, // Burde ha med (gir anbefaling til brukeren basert på varselet)
    val resources: List<Resource>? = null,
    val riskMatrixColor: String? = null, // Må ha med
    val severity: String? = null,
    val status: String? = null,
    val title: String? = null,
    val triggerLevel: String? = null,
    val type: String? = null,
    val web: String? = null
) {
    override fun toString(): String {
        return "Properties(area=$area, description=$description, event=$event, instruction=$instruction, title=$title, type=$type, web=$web)"
    }
}





