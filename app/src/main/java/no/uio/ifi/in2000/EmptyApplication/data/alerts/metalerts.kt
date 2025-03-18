package no.uio.ifi.in2000.EmptyApplication.data.alerts

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

class metalerts (http: HttpClient){
    // suspend funksjon som bruker url og returnerer liste av partier fra lenken ellers feilmelding

}