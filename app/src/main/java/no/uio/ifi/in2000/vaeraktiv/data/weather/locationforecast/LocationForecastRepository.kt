package no.uio.ifi.in2000.vaeraktiv.data.weather.locationforecast

import javax.inject.Inject

class LocationForecastRepository @Inject constructor(private val locationForecastDataSource: LocationForecastDataSource){
    suspend fun getResponse(){

    }
}