package no.uio.ifi.in2000.vaeraktiv.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

class KeepAliveManager {
    private var job: Job? = null

    fun start() {
        // launches a background coroutine that pings a lightweight endpoint
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    (URL("https://clients3.google.com/generate_204")
                        .openConnection() as HttpURLConnection).run {
                        connectTimeout = 5_000
                        connect()
                        disconnect()
                    }
                } catch (_: Exception) { /* ignore */ }
                delay(30_000)
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}