package no.uio.ifi.in2000.vaeraktiv.data.places

import android.util.Log
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.net.SearchNearbyRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class placesRepository @Inject constructor(private val placesClient: PlacesClient) {

    suspend fun getAutocompletePredictions(query: String, sessionToken: AutocompleteSessionToken?): List<AutocompletePrediction> = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            throw IllegalArgumentException("Query cannot be blank")
        }
        if (sessionToken == null) {
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

    suspend fun getNearbyPlaces(center: LatLng): List<Place> = withContext(Dispatchers.IO) {
        val initialRadius: Double = 10000.0 // 10 km
        val maxRadius: Double = 50000.0 // 50 km
        val radiusIncrement: Double = 5000.0 // 5 km
        var places: List<Place> = emptyList()

        val includedTypes: List<String> = listOf(
        "park", "museum", "restaurant", "cafe",
        "tourist_attraction", "point_of_interest",
        "amusement_park", "aquarium", "art_gallery",
        "campground", "library", "movie_theater",
        "spa", "stadium", "zoo",
        )
        val excludedTypes: List<String> = listOf(
        "gas_station", "parking", "car_repair",
        "car_wash", "convenience_store", "bank",
        "atm", "grocery_or_supermarket", "hardware_store",
        "home_goods_store", "shopping_mall", "store"
        )
        val placeFields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.DISPLAY_NAME, Place.Field.PRIMARY_TYPE,
            Place.Field.PRIMARY_TYPE_DISPLAY_NAME, Place.Field.TYPES,
            Place.Field.ADDRESS, Place.Field.LAT_LNG,
        )

        var currentRadius = initialRadius

        while (places.isEmpty() && currentRadius <= maxRadius) {
            try {
                val request = SearchNearbyRequest.builder(
                    CircularBounds.newInstance(center, currentRadius),
                    placeFields
                    )
                    .setIncludedTypes(includedTypes)
                    .setExcludedTypes(excludedTypes)
                    .setMaxResultCount(20)
                    .build()

                val response = placesClient.searchNearby(request).await()
                places = response.places
                if (places.isNotEmpty()) {
                    Log.d("placesRepository", "Found ${places.size} nearby places within radius $currentRadius")
                } else {
                    Log.d("placesRepository", "No nearby places found within radius $currentRadius")
                }
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