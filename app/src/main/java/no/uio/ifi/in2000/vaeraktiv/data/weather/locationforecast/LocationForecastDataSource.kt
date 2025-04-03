package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

class LocationForecastDataSource @Inject constructor(@Named("prettyPrint-isLenient-ignoreUnknownKeys-Client") private val networkClient: NetworkClient) {
    suspend fun getResponse(url: String): LocationForecastResponse = withContext(Dispatchers.IO) {
        try {
            val response = networkClient.ktorHttpClient.get(url)
            val content = Json{ignoreUnknownKeys = true}.decodeFromString<LocationForecastResponse>(response.body<String>())
            return@withContext content
        } catch (e: Exception) {
            Log.e("LocationForecastDataSource", "Error getting forecast: ", e)
            throw e
        }
    }
}