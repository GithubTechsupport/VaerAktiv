package no.uio.ifi.in2000.EmptyApplication.network.httpclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class HttpClient {
    private val ktorHttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }
}