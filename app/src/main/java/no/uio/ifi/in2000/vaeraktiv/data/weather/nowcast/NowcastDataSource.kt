package no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.model.nowcast.NowcastResponse
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

class NowcastDataSource @Inject constructor(@Named("prettyPrint-isLenient-ignoreUnknownKeys-Client") private val networkClient: NetworkClient) {
    suspend fun getResponse(url: String): NowcastResponse = withContext(Dispatchers.IO) {
        try {
            val response = networkClient.ktorHttpClient.get(url)
            val forecastResponse = Json{ignoreUnknownKeys = true}.decodeFromString<NowcastResponse>(response.body<String>())
            return@withContext forecastResponse
        } catch (e: Exception) {
            Log.e("NowcastDataSource", "Error getting forecast: ", e)
            throw e
        }
    }
}