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

    @Synchronized
    fun addLocation(placeName: String, latitude: Double, longitude: Double) {
        val formattedPlaceName = placeName.lowercase().split(" ")
            .joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }

        val existingLocations = file.readLines()

        val alreadyExists = existingLocations.any { line ->
            val parts = line.split(",")
            parts.size == 3 && (
                    parts[0] == formattedPlaceName || (parts[1] == latitude.toString() && parts[2] == longitude.toString())
                    )
        }

        if (alreadyExists) return

        val needsNewline = file.readText().lastOrNull()?.let { it != '\n' } ?: false
        val textToAppend = if (needsNewline) "\n$formattedPlaceName,$latitude,$longitude\n"
        else "$formattedPlaceName,$latitude,$longitude\n"

        file.appendText(textToAppend)
    }

    @Synchronized
    fun deleteLocation(placeName: String) {
        val lines = file.readLines().filterNot { it.startsWith("$placeName,") }
        file.writeText(lines.joinToString("\n"))
    }

    fun getAllLocations(): List<String> {
        return file.readLines()
    }
}