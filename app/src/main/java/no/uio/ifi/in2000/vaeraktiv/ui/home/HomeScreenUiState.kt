package no.uio.ifi.in2000.vaeraktiv.ui.home

import no.uio.ifi.in2000.vaeraktiv.model.home.AlertData
import no.uio.ifi.in2000.vaeraktiv.model.home.DetailedForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForDay
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastForHour
import no.uio.ifi.in2000.vaeraktiv.model.home.ForecastToday

/**
 * UI state for the home screen: weather, activities, loading and errors.
 */
data class HomeScreenUiState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val locationName: String = "",
    val alerts: List<AlertData> = emptyList(),
    val weatherToday: ForecastToday? = null,
    val thisWeeksWeather: List<ForecastForDay> = emptyList(),
    val sunRiseSet: List<String> = emptyList(),
    val next24Hours: List<ForecastForHour> = emptyList(),
    val dayIntervals: List<List<DetailedForecastForDay>> = emptyList(),

    // errors
    val todaysWeatherError: String? = null,
    val loadingActivities: Set<Pair<Int, Int>> = emptySet(),
    // todays activities
    val isLoadingActivitiesToday: Boolean = false,
    val isErrorActivitiesToday: Boolean = false,
    val errorMessageActivitiesToday: String = "",
    // rest of the week
    val loadingFutureActivities: Set<Int> = emptySet(),
    val isErrorFutureActivities: Boolean = false,
    val errorMessageFutureActivities: String = "",
    // errors for other data
    val weatherTodayError: String? = null,
    val thisWeeksWeatherError: String? = null,
    val alertsError: String? = null,
    val sunRiseSetError: String? = null,
    val next24HoursError: String? = null,
    val dayIntervalsError: String? = null
)