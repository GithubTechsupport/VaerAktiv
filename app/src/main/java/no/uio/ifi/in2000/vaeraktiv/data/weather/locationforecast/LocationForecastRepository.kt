package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast


import no.uio.ifi.in2000.vaeraktiv.model.locationforecast.LocationForecastResponse
import javax.inject.Inject

class LocationForecastRepository @Inject constructor(locationForecastDataSource: LocationForecastDataSource){
    private val loc = locationForecastDataSource
    private var forecasts: MutableMap<Pair<String, String>, LocationForecastResponse?> = mutableMapOf()
    private var response: LocationForecastResponse? = null
    suspend fun getUpdate(lat: String, lon: String): LocationForecastResponse? {

        response = loc.getResponse("https://api.met.no/weatherapi/locationforecast/2.0/complete?lat=$lat&lon=$lon")
        forecasts[Pair(lat, lon)] = response
        return response
    }

    suspend fun getForecast(lat: String, lon: String): LocationForecastResponse? {
        if (Pair(lat, lon) !in forecasts.keys){
            return getUpdate(lat, lon)
        }

        return forecasts[Pair(lat, lon)]
    }
}

suspend fun main() {
    val d = LocationForecastDataSource()
    val loc = LocationForecastRepository(d)
    val test = loc.getUpdate("60", "11")
    println(loc.getForecast("60", "11")?.properties?.timeseries?.get(0))
    println(loc.getUpdate("60", "5")?.properties?.timeseries?.get(0))
    if (test != null) {
        println(test.properties.timeseries[0])
    }
}

