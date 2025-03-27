package no.uio.ifi.in2000.vaeraktiv.network.connection


import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import no.uio.ifi.in2000.vaeraktiv.network.connection.NetworkConnection


/* This is a composable function that is used to observe the network status of the device.
 it takes in a function that is called when the network status changes true or false.
 sinse this is a mor complex composable function there are more comments in the code so it is easier to understand.

 This will always observe the network status of the device.
 */
@Composable
fun NetworkObserver(onNetworkStatusChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    var networkCallback by remember { mutableStateOf<ConnectivityManager.NetworkCallback?>(null) } // networkCallback is a variable that is used to get the network status.
    var isOnline by remember { mutableStateOf(NetworkConnection.isOnline(context)) }

    LaunchedEffect(Unit) {
        onNetworkStatusChanged(isOnline) // Send initial status
    }

    DisposableEffect(Unit) { // DisposableEffect is used to dispose of the network callback when the composable is disposed. Witch means when the composable is removed from the composition.
        val networkCallbackInner = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) { // onAvailable is a function that is called when the network is available.
                isOnline = true
                onNetworkStatusChanged(true)
            }
            override fun onLost(network: Network) { // onLost is a function that is called when the network is lost.
                isOnline = false
                onNetworkStatusChanged(false)
            }
        } // networkCallbackInner is a variable that is used to get the network status.

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallbackInner)
        networkCallback = networkCallbackInner // Store the callback

        onDispose { // onDispose is a function that is called when the composable is disposed.
            connectivityManager.unregisterNetworkCallback(networkCallbackInner)
        }
    }
}