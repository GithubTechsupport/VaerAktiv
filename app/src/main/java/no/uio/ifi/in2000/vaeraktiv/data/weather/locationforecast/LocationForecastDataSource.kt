package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.vaeraktiv.model.Ai.Prompt
import no.uio.ifi.in2000.vaeraktiv.model.loactionforecast.LocationForecastResponse

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