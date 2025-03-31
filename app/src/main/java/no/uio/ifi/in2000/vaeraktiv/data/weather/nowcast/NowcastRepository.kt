package no.uio.ifi.in2000.vaeraktiv.data.weather.nowcast

import no.uio.ifi.in2000.vaeraktiv.model.nowcast.NowcastResponse
import javax.inject.Inject

class NowcastRepository @Inject constructor(nowcastDataSource: NowcastDataSource){
    private val loc = nowcastDataSource
    private var forecasts: MutableMap<Pair<String, String>, NowcastResponse?> = mutableMapOf()
    private var response: NowcastResponse? = null
    suspend fun getUpdate(lat: String, lon: String): NowcastResponse? {

        response = loc.getResponse("https://api.met.no/weatherapi/nowcast/2.0/complete?lat=$lat&lon=$lon")
        forecasts[Pair(lat, lon)] = response
        return response
    }

    suspend fun getForecast(lat: String, lon: String): NowcastResponse? {
        if (Pair(lat, lon) !in forecasts.keys){
            return getUpdate(lat, lon)
        }

        return forecasts[Pair(lat, lon)]
    }
}

suspend fun main() {
    val d = NowcastDataSource()
    val loc = NowcastRepository(d)
    val test = loc.getUpdate("60", "11")
    println(loc.getForecast("60", "11")?.properties?.timeseries?.get(0))
    println(loc.getUpdate("60", "5")?.properties?.timeseries?.get(0))
    if (test != null) {
        println(test.properties.timeseries[0])
    }
}

