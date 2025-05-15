package no.uio.ifi.in2000.vaeraktiv.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import no.uio.ifi.in2000.vaeraktiv.model.ai.*
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import javax.inject.Inject
import javax.inject.Named

private val json = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        polymorphic(ActivitySuggestion::class) { //define the base class and the possible subclasses to deserialize into.
            subclass(CustomActivitySuggestion::class)
            subclass(PlaceActivitySuggestion::class)
            subclass(StravaActivitySuggestion::class)
        }
    }
}

/**
 * Fetches and decodes AI-generated activity suggestions via AiClient.
 */
class AiRepository @Inject constructor(
    @Named("OpenAi-Client") private val client: AiClient
) {
    /**
     * Retrieves a full-day SuggestedActivities object.
     */
    suspend fun getSuggestionsForOneDay(
        prompt: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String = ""
    ): SuggestedActivities = withContext(Dispatchers.IO) {
        try {
            val response = client.getSuggestionsForOneDay(prompt, nearbyPlaces, routes, preferences, exclusion)
                    ?: throw IllegalArgumentException("Response is null")
            return@withContext json.decodeFromString<SuggestedActivities>(response)
        } catch (e: Exception) {
            throw e
        }
    }

    /**
     * Retrieves a single ActivitySuggestion for a given day.
     */
    suspend fun getSingleSuggestionForDay(
        prompt: FormattedForecastDataForPrompt,
        nearbyPlaces: NearbyPlacesSuggestions,
        routes: RoutesSuggestions,
        preferences: String,
        exclusion: String = ""
    ) = withContext(Dispatchers.IO) {
        try {
            val response = client.getSingleSuggestionForDay(prompt, nearbyPlaces, routes, preferences, exclusion)
                ?: throw IllegalArgumentException("Response is null")

            return@withContext json.decodeFromString<ActivitySuggestion>(response)
        } catch (e: Exception) {
            throw e
        }
    }
}
