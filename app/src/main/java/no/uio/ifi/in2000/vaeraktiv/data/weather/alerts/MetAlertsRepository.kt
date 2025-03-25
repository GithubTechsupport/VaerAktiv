package no.uio.ifi.in2000.vaeraktiv.data.weather.alerts

import no.uio.ifi.in2000.vaeraktiv.model.metalerts.FeaturesResponse
import javax.inject.Inject

class MetAlertsRepository @Inject constructor(private val metAlertsDataSource: MetAlertsDataSource) {
    suspend fun retrieveAlertInfo(): FeaturesResponse? {
        return metAlertsDataSource.retriveAlertInfo()
    }

}