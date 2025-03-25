package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts


import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.vaeraktiv.model.metalerts.FeaturesResponse
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class MetAlertsDataSource{
    private val ktorHttpClient = HttpClient(CIO) {
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            header("User-Agent", "EmptyApplication/1.0")
        }
    }

    suspend fun retriveAlertInfo() : FeaturesResponse? = withContext(Dispatchers.IO) {
        return@withContext try {
            //Log.i("MetAlertsDataSource", "Fetching data...")
            val response : FeaturesResponse = ktorHttpClient.get("https://api.met.no/weatherapi/metalerts/2.0/current.json").body()
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
    val dataSource = MetAlertsDataSource()
    val response = dataSource.retriveAlertInfo()
    response?.features?.forEach { feature ->
        println("Feature type: ${feature.type}")
        println("Geometry type: ${feature.geometry.type}")
        println("Coordinates: ${feature.geometry.getCoordinatesAsList()}")
    }
    println("Main response: $response")
}