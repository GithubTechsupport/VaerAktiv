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
        val addresses = geocoder.getCoordinatesFromLocation(placeName)
        if (addresses !=null) {
            dataSource.addLocation(String.format("%.3f", addresses.first).toDouble(), String.format("%.3f", addresses.second).toDouble(), placeName)
        }
        Log.d("address", "$addresses")

    }

    fun deleteLocationByName(placeName: String) {
        dataSource.deleteLocation(placeName)
    }

    fun getAllLocations(): List<String> {
        return dataSource.getAllLocations()
    }
}


