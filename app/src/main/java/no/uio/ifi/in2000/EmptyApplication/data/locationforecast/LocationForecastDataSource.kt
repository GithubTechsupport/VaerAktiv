package no.uio.ifi.in2000.EmptyApplication.data.locationforecast

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.EmptyApplication.model.loactionforecast.LocationForecastResponse

class LocationForecastDataSource {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getResponse(url: String): LocationForecastResponse = withContext(Dispatchers.IO) {
        val response = client.get(url)
        val forecastResponse = Json{ignoreUnknownKeys = true}.decodeFromString<LocationForecastResponse>(response.body<String>())
        return@withContext forecastResponse
    }
}

suspend fun main() {
    val loc = LocationForecastDataSource()
    val response = loc.getResponse("https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=60&lon=11")
    println("${response.properties.timeseries[0]}\n------------------------------------------\n${response.properties.timeseries[1]}\n------------------------------------------\n${response.properties.timeseries[2]}")
}