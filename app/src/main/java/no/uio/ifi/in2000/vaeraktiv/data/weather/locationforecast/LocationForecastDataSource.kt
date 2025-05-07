package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast

import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

private val json = Json { ignoreUnknownKeys = true }

class LocationForecastDataSource @Inject constructor(@Named("prettyPrint-isLenient-ignoreUnknownKeys-Client") private val networkClient: NetworkClient) {
    suspend fun getResponse(url: String): LocationForecastResponse = withContext(Dispatchers.IO) {
        try {
            val response = networkClient.ktorHttpClient.get(url)
            val content = json.decodeFromString<LocationForecastResponse>(response.body<String>())
            return@withContext content
        } catch (e: Exception) {
            Log.e("LocationForecastDataSource", "Error getting forecast: ", e)
            throw e
        }
    }
}