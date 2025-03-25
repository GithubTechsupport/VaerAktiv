package no.uio.ifi.in2000.vaeraktiv.data.alerts

import no.uio.ifi.in2000.vaeraktiv.model.metalerts.FeaturesResponse

class MetAlertsRepository (private val dataSource: MetAlertsDataSource) {
    suspend fun retrieveAlertInfo(): FeaturesResponse? {
        return dataSource.retriveAlertInfo()
    }
}