package no.uio.ifi.in2000.vaeraktiv.data.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import no.uio.ifi.in2000.vaeraktiv.model.ai.ActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.CustomActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.FormattedForecastDataForPrompt
import no.uio.ifi.in2000.vaeraktiv.model.ai.PlacesActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.RoutesSuggestions
import no.uio.ifi.in2000.vaeraktiv.model.ai.StravaActivitySuggestion
import no.uio.ifi.in2000.vaeraktiv.model.ai.SuggestedActivities
import no.uio.ifi.in2000.vaeraktiv.model.ai.places.NearbyPlacesSuggestions
import no.uio.ifi.in2000.vaeraktiv.network.aiclient.AiClient
import javax.inject.Inject
import javax.inject.Named

class AiRepository @Inject constructor(@Named("OpenAi-Client") private val client: AiClient) {
    suspend fun getSuggestionsForOneDay(prompt: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String = ""): SuggestedActivities = withContext(Dispatchers.IO) {
        try {
            val response = client.getSuggestionsForOneDay(prompt, nearbyPlaces, routes, preferences, exclusion)
            if (response == null) {
                throw IllegalArgumentException("Response is null")
            }
            return@withContext Json {
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    polymorphic(ActivitySuggestion::class) {
                        subclass(CustomActivitySuggestion::class)
                        subclass(PlacesActivitySuggestion::class)
                        subclass(StravaActivitySuggestion::class)
                    }
                }
            }.decodeFromString<SuggestedActivities>(response)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getSingleSuggestionForDay(prompt: FormattedForecastDataForPrompt, nearbyPlaces: NearbyPlacesSuggestions, routes: RoutesSuggestions, preferences: String, exclusion: String = ""): ActivitySuggestion = withContext(Dispatchers.IO) {
        try {
            val response = client.getSingleSuggestionForDay(prompt, nearbyPlaces, routes, preferences, exclusion)

            if (response == null) {
                throw IllegalArgumentException("Response is null")
            }
            return@withContext Json {
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    polymorphic(ActivitySuggestion::class) {
                        subclass(CustomActivitySuggestion::class)
                        subclass(PlacesActivitySuggestion::class)
                        subclass(StravaActivitySuggestion::class)
                    }
                }
            }.decodeFromString<ActivitySuggestion>(response)
        } catch (e: Exception) {
            throw e
        }
    }
}