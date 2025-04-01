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
        file.appendText("$placeName,$latitude,$longitude\n")
    }

    fun deleteLocation(placeName: String) {
        val lines = file.readLines().filterNot { it.startsWith("$placeName,") }
        file.writeText(lines.joinToString("\n"))
    }

    fun getAllLocations(): List<String> {
        return file.readLines()
    }
}