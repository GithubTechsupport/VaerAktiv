package no.uio.ifi.in2000.vaeraktiv.data.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeocoderClass @Inject constructor(private val context: Context) {

    private val geocoder = Geocoder(context, Locale.forLanguageTag("nb-NO"))
    /**
     * Converts a location name (e.g., "Oslo, Norway") into its latitude and longitude coordinates.
     * @param locationName The name of the location to geocode.
     * @return A Pair containing the latitude and longitude, or null if the location cannot be found.
     */
    fun getCoordinatesFromLocation(locationName: String): Pair<String, Pair<Double, Double>>? {
        try {
            val addressList: List<Address>? = geocoder.getFromLocationName(locationName, 1)

            if (!addressList.isNullOrEmpty()) {
                val address: Address = addressList[0]

                val returnedName = address.locality ?: address.featureName ?: ""


                val latitude: Double = address.latitude
                val longitude: Double = address.longitude
                return Pair(returnedName, Pair(latitude, longitude))
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

    fun getLocationFromCoordinates(coordinates: Pair<String, String>): Address? {

        try {
            val addressList: List<Address>? = geocoder.getFromLocation(coordinates.first.toDouble(), coordinates.second.toDouble(), 3)
            if (!addressList.isNullOrEmpty()) {
                val address: Address = addressList[0]
                return address
            } else {
                // Location not found
                return null
            }
        } catch (e: IOException) {
            // Handle network or other I/O errors
            throw e
        } catch (e: Exception) {
            // Handle other exceptions
            throw e
        }
    }
}