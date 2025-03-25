package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable

@Serializable
data class Properties(
    val altitude_above_sea_level: Int? = null,
    val area: String? = null, // må ha med
    val awarenessResponse: String? = null,
    val awarenessSeriousness: String? = null,
    val awareness_level: String? = null, // burde ha med (gir alvorlighetsgraden det varsles om)
    val awareness_type: String? = null, // kan ha med (gir hva det varsles om)
    val ceiling_above_sea_level: Int? = null,
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
    val riskMatrixColor: String? = null,
    val severity: String? = null,
    val status: String? = null,
    val title: String? = null,
    val triggerLevel: String? = null,
    val type: String? = null,
    val web: String? = null
)





