package no.uio.ifi.in2000.vaeraktiv.network.connection

// NetworkObserver.kt
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

@Composable
fun NetworkObserver(onNetworkStatusChanged: (Boolean) -> Unit) {
    val context = LocalContext.current
    var networkCallback by remember { mutableStateOf<ConnectivityManager.NetworkCallback?>(null) }
    var isOnline by remember { mutableStateOf(NetworkConnection.isOnline(context)) }

    LaunchedEffect(Unit) {
        onNetworkStatusChanged(isOnline) // Send initial status
    }

    DisposableEffect(Unit) {
        val networkCallbackInner = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isOnline = true
                onNetworkStatusChanged(true)
            }
            override fun onLost(network: Network) {
                isOnline = false
                onNetworkStatusChanged(false)
            }
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallbackInner)
        networkCallback = networkCallbackInner // Store the callback

        onDispose {
            connectivityManager.unregisterNetworkCallback(networkCallbackInner)
        }
    }
}