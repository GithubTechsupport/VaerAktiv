package no.uio.ifi.in2000.vaeraktiv.data.location

import java.util.Locale
import javax.inject.Inject

/**
 * Repository for adding, deleting and listing favorite locations by name.
 */
class FavoriteLocationRepository @Inject constructor(
    private val dataSource: FavoriteLocationDataSource,
    private val geocoder: GeocoderClass
) {
    /** Adds a location by name after geocoding. */
    fun addLocationByName(placeName: String) {
        val response = geocoder.getCoordinatesFromLocation(placeName)
        val name = placeName.split(",")[0]
        val addresses = response?.second

        if (addresses != null) {
            dataSource.addLocation(
                name,
                String.format(Locale.US, "%.3f", addresses.first).toDouble(),
                String.format(Locale.US, "%.3f", addresses.second).toDouble()
            )
        }
    }

    /** Removes a favorite location by its name. */
    fun deleteLocationByName(placeName: String) {
        dataSource.deleteLocation(placeName)
    }

    /** Returns stored locations as "name,lat,lon" strings. */
    fun getAllLocations(): List<String> {
        return dataSource.getAllLocations()
    }
}
