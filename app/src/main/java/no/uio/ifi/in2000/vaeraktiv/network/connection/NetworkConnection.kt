package no.uio.ifi.in2000.vaeraktiv.network.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
/*
* This object is used to check if the device is connected to the internet or not.
*
* */
object NetworkConnection {
    // isOnline takes in a context and returns a boolean. A context is used to get the connectivity manager.
    fun isOnline(context: Context): Boolean {
        //connectivityManager is a system service that is used to check the network status.
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // network is a variable that is used to get the active network.
        val network = connectivityManager.activeNetwork ?: return false
        // activeNetwork is a variable that is used to get the network capabilities. This is used to check if the network is connected to the internet or not.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when { // returns true if the network is connected to the internet.
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
}