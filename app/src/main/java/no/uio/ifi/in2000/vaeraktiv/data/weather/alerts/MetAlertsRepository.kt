package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts

import no.uio.ifi.in2000.vaeraktiv.model.metalerts.Features
import javax.inject.Inject

class MetAlertsRepository @Inject constructor(private val metAlertsDataSource: MetAlertsDataSource) {

    private var alertsCache: List<Features> = emptyList()

    private suspend fun fetchAlerts() {
        val response = metAlertsDataSource.retriveAlertInfo()
        alertsCache = response?.features ?: emptyList()
    }
    
    suspend fun getAlertsForLocation(lat: String, lon: String): List<Features> {
        // Gathers all active alerts and caches it alerts arent already fetched.
        if (alertsCache.isEmpty()) {
            fetchAlerts()
        }

        return alertsCache.filter { feature ->
            val polygon = feature.geometry.getCoordinatesAsList()
            isPointInPolygon(lat, lon, polygon)
        }
    }

    // Point-in-polygon algorithm (ray-casting)
    /**
     * Checks if a point is inside a given polygon.
     * @param lat The latitude of the point.
     * @param lon The longitude of the point.
     * @param polygon The list of coordinates defining the polygon.
     * @return True if the point is inside the polygon, false otherwise.
     */
    private fun isPointInPolygon(lat: String, lon: String, polygon: List<List<List<Float>>>): Boolean {
        val latitude = lat.toDoubleOrNull() ?: return false
        val longitude = lon.toDoubleOrNull() ?: return false

        var inside = false
        for (ring in polygon) {
            val n = ring.size
            var j = n - 1
            for (i in 0 until n) {
                val (xi, yi) = ring[i]
                val (xj, yj) = ring[j]
                if ((yi > latitude) != (yj > latitude) && longitude < (xj - xi) * (latitude - yi) / (yj - yi) + xi) {
                    inside = !inside
                }
                j = i
            }
        }
        return inside
    }
}