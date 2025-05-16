package no.uio.ifi.in2000.vaeraktiv.data.places

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Fetches place autocomplete predictions and nearby place results.
 */
class PlacesRepository @Inject constructor(
    private val placesClient: PlacesClient
) {

    /**
     * Retrieves autocomplete suggestions for a query.
     */
    suspend fun getAutocompletePredictions(
        query: String,
        sessionToken: AutocompleteSessionToken?
    ): List<AutocompletePrediction> = withContext(Dispatchers.IO) {
        require(query.isNotBlank()) {
            throw IllegalArgumentException("Query cannot be blank")
        }
        requireNotNull(sessionToken) {
            throw IllegalArgumentException("Session token cannot be null")
        }
        try {
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(sessionToken)
                .setQuery(query)
                .setRegionCode("NO")
                .setCountries(listOf("NO"))
                .build()

            val response = placesClient.findAutocompletePredictions(request).await()
            return@withContext response.autocompletePredictions
        } catch (e: Exception) {
            Log.e("placesRepository", "Error fetching autocomplete predictions", e)
            return@withContext emptyList()
        }
    }

    /**
     * Searches for nearby places around center within expanding radius.
     */
    suspend fun getNearbyPlaces(center: LatLng): List<Place> = withContext(Dispatchers.IO) {
        val initialRadius = 10000.0 // 10 km
        val maxRadius = 50000.0 // 50 km
        val radiusIncrement = 5000.0 // 5 km
        var places: List<Place> = emptyList()

        val includedTypes: List<String> = listOf(
        "adventure_sports_center", "skateboard_park", "hiking_area",
        "water_park", "botanical_garden", "cycling_park",
        "beach", "athletic_field", "fitness_center",
        "gym", "ice_skating_rink", "ski_resort", "sports_activity_location",
        "sports_club", "sports_complex", "swimming_pool"
        )


        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.FORMATTED_ADDRESS, Place.Field.LOCATION,
            Place.Field.PRIMARY_TYPE, Place.Field.PRIMARY_TYPE_DISPLAY_NAME, Place.Field.TYPES,
        )

        var currentRadius = initialRadius

        while (places.isEmpty() && currentRadius <= maxRadius) { // tries to find locations in the specified radius range until a location is found.
            try {
                val request = SearchNearbyRequest.builder(
                    CircularBounds.newInstance(center, currentRadius),
                    placeFields
                    )
                    .setIncludedTypes(includedTypes)
                    .setMaxResultCount(20)
                    .build()

                val response = placesClient.searchNearby(request).await()
                places = response.places
            } catch (e: Exception) {
                Log.e("placesRepository", "Error fetching nearby places within radius $currentRadius", e)
                return@withContext emptyList()
            }
            currentRadius += radiusIncrement
        }
        if (places.isEmpty()) {
            Log.w("placesRepository", "No nearby places found within max radius $maxRadius")
        }
        return@withContext places
    }

}
