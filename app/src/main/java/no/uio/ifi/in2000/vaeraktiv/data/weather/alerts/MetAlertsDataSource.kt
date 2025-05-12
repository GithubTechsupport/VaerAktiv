package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.FeaturesResponse
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

class MetAlertsDataSource @Inject constructor(@Named("prettyPrint-ignoreUnknownKeys-Client") private val networkClient: NetworkClient){
    suspend fun retrieveAlertInfo() : FeaturesResponse = withContext(Dispatchers.IO) {
        return@withContext try {
            val response : FeaturesResponse = networkClient.ktorHttpClient
                .get("https://api.met.no/weatherapi/metalerts/2.0/current.json") {
                    header("User-Agent", "VaerAktiv/1.0")
                }.body()
            response
        } catch (e : Exception){
            throw e
        }
    }
}
