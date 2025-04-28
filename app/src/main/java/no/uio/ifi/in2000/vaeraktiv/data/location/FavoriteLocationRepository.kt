package no.uio.ifi.in2000.vaeraktiv.data.location

import android.annotation.SuppressLint
import android.util.Log
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
        Log.d("FavoriteLocationRepository", "addLocationByName: $name")
        if (addresses != null) {
            if (name != null) {
                dataSource.addLocation(
                    name,
                    String.format("%.3f", addresses.first).toDouble(),
                    String.format("%.3f", addresses.second).toDouble()
                )
            }
        }
    }

    fun deleteLocationByName(placeName: String) {
        dataSource.deleteLocation(placeName)
    }

    fun getAllLocations(): List<String> {
        return dataSource.getAllLocations()
    }
}


