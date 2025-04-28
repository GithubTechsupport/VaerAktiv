package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast


import android.util.Log
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.TimeSeries
import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.Units
import javax.inject.Inject

class LocationForecastRepository @Inject constructor(
    private val dataSource: LocationForecastDataSource
){

    private var forecasts: MutableMap<Pair<String, String>, LocationForecastResponse?> = mutableMapOf()
    private var response: LocationForecastResponse? = null
    suspend fun getUpdate(lat: String, lon: String): LocationForecastResponse? {

        response = dataSource.getResponse("https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=$lat&lon=$lon")
        forecasts[Pair(lat, lon)] = response
        return response
    }

    suspend fun getForecast(lat: String, lon: String): LocationForecastResponse? {
        if (Pair(lat, lon) !in forecasts.keys){
            return getUpdate(lat, lon)
        }

        return forecasts[Pair(lat, lon)]
    }

    suspend fun getForecastByDay(lat: String, lon: String): Pair<List<Pair<String, List<TimeSeries>>>?, Units?> {
        val forecast = getForecast(lat, lon)
        val timeseries = forecast?.properties?.timeseries
        val units = forecast?.properties?.meta?.units
        val groupedTimeSeries = timeseries?.groupBy { it.time.substring(0, 10) }?.toList()
        return Pair(groupedTimeSeries, units)
    }

    suspend fun getNext24Hours(lat: String, lon: String): List<TimeSeries>? {
        val forecast = getForecast(lat, lon)
        val timeseries = forecast?.properties?.timeseries
        return timeseries?.drop(1)?.take(24)
    }

}

