package no.uio.ifi.in2000.vaeraktiv.data.location

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

data class FavoriteLocation(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

class FavoriteLocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fileName = "favorite_locations.json"
    private val file: File = File(context.filesDir, fileName)
    private val gson = Gson()

    init {
        if (!file.exists()) {
            file.writeText("[]") // initialize with empty JSON array
        }
    }

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

    @Synchronized
    fun deleteLocation(placeName: String) {
        val formattedPlaceName = placeName.trim().lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar(Char::uppercase) }

        val locations = getAllLocationObjects().filterNot {
            it.name.equals(formattedPlaceName, ignoreCase = true)
        }
        saveLocationsToFile(locations)
    }

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