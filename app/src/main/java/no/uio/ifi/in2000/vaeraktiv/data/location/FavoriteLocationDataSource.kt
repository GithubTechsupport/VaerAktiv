package no.uio.ifi.in2000.vaeraktiv.data.location

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FavoriteLocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fileName = "favorite_locations.txt"
    private val file: File = File(context.filesDir, fileName)

    init {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    fun addLocation(latitude: Double, longitude: Double, placeName: String) {
        val formattedPlaceName = placeName.lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        val existingLocations = file.readLines()

        if (existingLocations.any { it.startsWith("$formattedPlaceName,") }) {
            return
        }

        file.appendText("$formattedPlaceName,$latitude,$longitude\n")
    }

    fun deleteLocation(placeName: String) {
        val lines = file.readLines().filterNot { it.startsWith("$placeName,") }
        file.writeText(lines.joinToString("\n"))
    }

    fun getAllLocations(): List<String> {
        return file.readLines()
    }
}