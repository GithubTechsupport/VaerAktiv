package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts


import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.FeaturesResponse
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import no.uio.ifi.in2000.vaeraktiv.network.httpclient.NetworkClient
import javax.inject.Named

class MetAlertsDataSource @Inject constructor(@Named("prettyPrint-ignoreUnknownKeys-Client") private val networkClient: NetworkClient){
    suspend fun retriveAlertInfo() : FeaturesResponse? = withContext(Dispatchers.IO) {
        return@withContext try {
            //Log.i("MetAlertsDataSource", "Fetching data...")
            val response : FeaturesResponse = networkClient.ktorHttpClient
                .get("https://api.met.no/weatherapi/metalerts/2.0/current.json") {
                    header("User-Agent", "VaerAktiv/1.0")
                }.body()
            println(response)
            response
        } catch (e : Exception){
            //Log.e("MetAlertsDataSource", "Error fetching data: ${e.message}")
            e.printStackTrace()
            null
        }
    }
}

fun main() = runBlocking{
    val prettyPrintIgnoreUnknownJson = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    val dataSource = MetAlertsDataSource(networkClient = NetworkClient(prettyPrintIgnoreUnknownJson))
    val response = dataSource.retriveAlertInfo()
    response?.features?.forEach { feature ->
        println("Feature type: ${feature.type}")
        println("Geometry type: ${feature.geometry.type}")
        println("Coordinates: ${feature.geometry.getCoordinatesAsList()}")
    }
    println("Main response: $response")
}