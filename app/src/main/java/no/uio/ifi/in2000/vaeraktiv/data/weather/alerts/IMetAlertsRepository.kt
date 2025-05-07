package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts

import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Features

fun interface IMetAlertsRepository {
    suspend fun getAlertsForLocation(lat: String, lon: String): List<Features>
}