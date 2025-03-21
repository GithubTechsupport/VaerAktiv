package no.uio.ifi.in2000.EmptyApplication.data.locationforecast

import android.content.Context
import android.location.Geocoder
import android.location.Address
import java.io.IOException
import java.util.Locale

class GeocoderClass(val context: Context) {

    private val geocoder = Geocoder(context, Locale.getDefault())
    /**
     * Converts a location name (e.g., "Oslo, Norway") into its latitude and longitude coordinates.
     * @param locationName The name of the location to geocode.
     * @return A Pair containing the latitude and longitude, or null if the location cannot be found.
     */
    fun getLocationCoordinates(locationName: String): Pair<Double, Double>? {
        try {
            val addressList: List<Address>? = geocoder.getFromLocationName(locationName, 1)

            if (!addressList.isNullOrEmpty()) {
                val address: Address = addressList[0]
                val latitude: Double = address.latitude
                val longitude: Double = address.longitude
                return Pair(latitude, longitude)
            } else {
                // Location not found
                return null
            }
        } catch (e: IOException) {
            // Handle network or other I/O errors
            e.printStackTrace()
            return null
        } catch (e: IllegalArgumentException) {
            // Handle invalid arguments (e.g., null locationName)
            e.printStackTrace()
            return null
        }
    }
}