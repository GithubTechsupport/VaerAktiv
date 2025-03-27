package no.uio.ifi.in2000.vaeraktiv.data.weather.sunrise

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import no.uio.ifi.in2000.vaeraktiv.model.sunrise.SunData
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Inject
import javax.inject.Named

class SunriseDataSource @Inject constructor(@Named("ignoreUnknownKeys-Client") private val networkClient: NetworkClient){
//    private val ktorHttpClient = HttpClient(CIO) {
//        install(ContentNegotiation) {
//            json(Json{
//                ignoreUnknownKeys = true
//            })
//        }
//    }

    suspend fun getSunrise(lat: String, lon: String, date: String) : SunData? {
        try {
            // TODO : fikse at den henter dagens dato og plassering
            val response: HttpResponse = networkClient.ktorHttpClient.get("https://in2000.api.met.no/weatherapi/sunrise/3.0/sun?lat=$lat&lon=$lon&date=$date&offset=+01:00")
            return response.body()
        } catch (e: Exception) {
            return null
        } finally {
            networkClient.ktorHttpClient.close()//ktorHttpClient.close()
        }
    }
}
