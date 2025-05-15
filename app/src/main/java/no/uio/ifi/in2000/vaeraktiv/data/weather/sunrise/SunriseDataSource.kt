package no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunData
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

/**
 * Fetches sunrise and sunset times from the MET Sunrise API.
 */
class SunriseDataSource @Inject constructor(
    @Named("ignoreUnknownKeys-Client") private val networkClient: NetworkClient
) {

    /** Returns SunData for given lat, lon and date or throws on error. */
    suspend fun getSunrise(lat: String, lon: String, date: String): SunData? = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = networkClient.ktorHttpClient.get("https://in2000.api.met.no/weatherapi/sunrise/3.0/sun?lat=$lat&lon=$lon&date=$date&offset=+01:00")
            return@withContext response.body()
        } catch (e: Exception) {
            throw e
        }
    }
}
