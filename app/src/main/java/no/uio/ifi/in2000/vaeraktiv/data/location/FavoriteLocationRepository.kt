package no.uio.ifi.in2000.vaeraktiv.data.location

import android.annotation.SuppressLint
import java.util.Locale
import javax.inject.Inject

class FavoriteLocationRepository @Inject constructor(
    private val dataSource: FavoriteLocationDataSource,
    private val geocoder: GeocoderClass
) {


    @SuppressLint("DefaultLocale")
    fun addLocationByName(placeName: String) {
        val response = geocoder.getCoordinatesFromLocation(placeName)
        val name = placeName.split(",")[0]
        val addresses = response?.second

        if (addresses != null) {
            dataSource.addLocation(
                name,
                String.format(Locale.US, "%.3f", addresses.first).toDouble(), // Save the place name and coordinates to the first 3 digits along with the
                String.format(Locale.US, "%.3f", addresses.second).toDouble()
            )
        }
    }

    fun deleteLocationByName(placeName: String) {
        dataSource.deleteLocation(placeName)
    }

    fun getAllLocations(): List<String> {
        return dataSource.getAllLocations()
    }
}


