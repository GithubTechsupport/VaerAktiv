package no.uio.ifi.in2000.vaeraktiv.data.location

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.FavoriteLocation
import java.io.File
import javax.inject.Inject

/**
 * Persists favorite locations to JSON file and retrieves them.
 */
class FavoriteLocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fileName = "favorite_locations.json"
    private val file: File = File(context.filesDir, fileName)
    private val gson = Gson()

    init {
        if (!file.exists()) {
            file.writeText("[]")
        }
    }

    /** Adds a formatted location if not already present. */
    @Synchronized
    fun addLocation(placeName: String, latitude: Double, longitude: Double) {
        val formattedPlaceName = placeName.trim().lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

        val locations = getAllLocationObjects().toMutableList()

        val alreadyExists = locations.any {
            it.name.equals(formattedPlaceName, ignoreCase = true) ||
                    (it.latitude == latitude && it.longitude == longitude)
        }

        if (!alreadyExists) {
            locations.add(FavoriteLocation(formattedPlaceName, latitude, longitude))
            saveLocationsToFile(locations)
        }
    }

    /** Deletes a location by its formatted name. */
    @Synchronized
    fun deleteLocation(placeName: String) {
        val formattedPlaceName = placeName.trim().lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

        val locations = getAllLocationObjects().filterNot {
            it.name.equals(formattedPlaceName, ignoreCase = true)
        }
        saveLocationsToFile(locations)
    }

    /** Returns all locations as CSV strings. */
    fun getAllLocations(): List<String> {
        return getAllLocationObjects().map {
            "${it.name},${it.latitude},${it.longitude}"
        }
    }

    private fun getAllLocationObjects(): List<FavoriteLocation> {
        val type = object : TypeToken<List<FavoriteLocation>>() {}.type
        return gson.fromJson(file.readText(), type) ?: emptyList()
    }

    private fun saveLocationsToFile(locations: List<FavoriteLocation>) {
        file.writeText(gson.toJson(locations))
    }
}
