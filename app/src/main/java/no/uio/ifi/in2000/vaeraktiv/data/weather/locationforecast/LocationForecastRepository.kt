package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast

import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units
import javax.inject.Inject

class LocationForecastRepository @Inject constructor(
    private val dataSource: LocationForecastDataSource
) {

    private val forecasts: MutableMap<Pair<String, String>, LocationForecastResponse?> = mutableMapOf()
    private val baseUrl = "https://api.met.no/weatherapi/locationforecast/2.0/complete"

    suspend fun getUpdate(lat: String, lon: String): LocationForecastResponse {
        val url = "$baseUrl?lat=$lat&lon=$lon"
        val response = dataSource.getResponse(url)
        forecasts[Pair(lat, lon)] = response
        return response
    }

    suspend fun getForecast(lat: String, lon: String): LocationForecastResponse? {
        return forecasts.getOrPut(Pair(lat, lon)) { getUpdate(lat, lon) }
    }

    suspend fun getForecastByDay(lat: String, lon: String): Pair<List<Pair<String, List<TimeSeries>>>, Units?> {
        val forecast = getForecast(lat, lon) ?: return Pair(emptyList(), null)
        return extractDailyForecastData(forecast)
    }

    private fun extractDailyForecastData(forecast: LocationForecastResponse): Pair<List<Pair<String, List<TimeSeries>>>, Units?> {
        val timeseries = forecast.properties.timeseries
        val units = forecast.properties.meta.units

        val groupedTimeSeries = timeseries.groupBy { it.time.substring(0, 10) }.toList()
        return Pair(groupedTimeSeries, units)
    }

    suspend fun getNext24Hours(lat: String, lon: String): List<TimeSeries>? {
        val forecast = getForecast(lat, lon)
        return extractNext24Hours(forecast)
    }

    private fun extractNext24Hours(forecast: LocationForecastResponse?) : List<TimeSeries>? {
        val timeseries = forecast?.properties?.timeseries
        return timeseries?.drop(1)?.take(24)
    }
}