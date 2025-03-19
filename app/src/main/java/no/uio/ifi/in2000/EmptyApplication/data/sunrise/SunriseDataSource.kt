package no.uio.ifi.in2000.EmptyApplication.data.sunrise

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO

import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import no.uio.ifi.in2000.EmptyApplication.model.sunrise.SunData

class SunriseDataSource {
    private val ktorHttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json{
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getSunrise() : SunData? {
        try {
            // TODO : fikse at den henter dagens dato og plassering
            val response: HttpResponse = ktorHttpClient.get("https://in2000.api.met.no/weatherapi/sunrise/3.0/sun?lat=59.933333&lon=10.716667&date=2025-03-18&offset=+01:00")
            return response.body()
        } catch (e: Exception) {
            return null
        } finally {
            ktorHttpClient.close()
        }
    }
}
