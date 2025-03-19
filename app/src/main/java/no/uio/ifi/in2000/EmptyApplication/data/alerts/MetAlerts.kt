package no.uio.ifi.in2000.EmptyApplication.data.alerts


import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.EmptyApplication.model.metalerts.FeaturesResponse

class MetAlerts{

    private val ktorHttpClient = HttpClient(CIO) {
        install(ContentNegotiation){
            json()
        }
    }

    suspend fun retriveAlertInfo() : FeaturesResponse? {
        return try {
            val response : HttpResponse = ktorHttpClient.get("https://api.met.no/weatherapi/metalerts/2.0/current.json")
            val featuresResponse : FeaturesResponse = Json.decodeFromString(response.toString())
            println(featuresResponse)
            featuresResponse


        } catch (e : Exception){
            null
        }
    }

}