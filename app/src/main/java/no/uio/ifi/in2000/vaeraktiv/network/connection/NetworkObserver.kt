package no.uio.ifi.in2000.vaeraktiv.network.connection

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/* This is a composable function that is used to observe the network status of the device.
 it takes in a function that is called when the network status changes true or false.
 sinse this is a mor complex composable function there are more comments in the code so it is easier to understand.

 This will always observe the network status of the device.
 */

@Composable
fun NetworkObserver(onNetworkStatusChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    var networkCallback by remember { mutableStateOf<ConnectivityManager.NetworkCallback?>(null) }
    var isOnline by remember { mutableStateOf<Boolean?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isDebouncing by remember { mutableStateOf(false) }
    var isLostDebouncing by remember { mutableStateOf(false) }

    println("NetworkObserver: Composed")

    LaunchedEffect(Unit) {
        Log.d("NetworkObserver", "LaunchedEffect: Initial network state is ${NetworkConnection.isOnline(context)}")
    }

    DisposableEffect(Unit) {
        val networkCallbackInner = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                coroutineScope.launch {
                    isDebouncing = true
                    Log.d("NetworkObserver", "Network Available (debouncing)")
                    delay(500) // debounce time of 500 ms.
                    if (isDebouncing) {
                        Log.d("NetworkObserver", "Network Available (sending update)")
                        isOnline = true
                        onNetworkStatusChanged(true)
                    }
                    isLostDebouncing = false
                }
            }

            override fun onLost(network: Network) {
                coroutineScope.launch {
                    isDebouncing = false
                    isLostDebouncing = true
                    Log.d("NetworkObserver", "Network Lost (debouncing)")
                    delay(5000) // delay time of 5 seconds.
                    if (isLostDebouncing) {
                        Log.d("NetworkObserver", "Network Lost (sending update)")
                        isOnline = false
                        onNetworkStatusChanged(false)
                    }
                }
            }
        }

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallbackInner)
        networkCallback = networkCallbackInner

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallbackInner)
        }
    }
}