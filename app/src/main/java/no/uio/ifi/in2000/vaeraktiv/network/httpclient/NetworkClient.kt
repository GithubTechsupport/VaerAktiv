package no.uio.ifi.in2000.vaeraktiv.network.httpclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkClient @Inject constructor(private val json: Json) {
    private val userAgent = "VaerAktiv/1.0 (https://github.uio.no/IN2000-V25/team-31)"

    val ktorHttpClient = HttpClient(CIO) {
        install(HttpCache)
        install(ContentNegotiation) {
            json(json)
        }
        defaultRequest {
            headers {
                append(HttpHeaders.UserAgent, userAgent)
            }
        }
    }
}