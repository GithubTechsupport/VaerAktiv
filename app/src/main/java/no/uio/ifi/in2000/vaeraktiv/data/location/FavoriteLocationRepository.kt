package no.uio.ifi.in2000.vaeraktiv.data.location

import android.content.Context
import android.util.Log

import dagger.hilt.android.qualifiers.ApplicationContext
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast.LocationForecastRepository
import javax.inject.Inject

class FavoriteLocationRepository @Inject constructor(
    private val dataSource: FavoriteLocationDataSource,
    private val geocoder: GeocoderClass
) {


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


