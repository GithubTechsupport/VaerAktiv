  ```mermaid
classDiagram
    HomeScreen "1" o-- "1" HomeScreenViewModel : viewModel
    HomeScreenViewModel "1" o-- "1" WeatherRepository : repo
    WeatherRepository "1" o-- "1" LocationForecastRepository
    WeatherRepository "1" o-- "1" PlacesRepository
    WeatherRepository "1" o-- "1" StravaRepository
    WeatherRepository "1" o-- "1" PreferencesRepository
    WeatherRepository "1" o-- "1" AiRepository
    AiRepository "1" o-- "1" AiClient
    PlacesClient "1" o-- "1" PlacesRepository
    HomeScreenUiState "1" o-- "1" HomeScreenViewModel

    class HomeScreenUiState {

    }

    class HomeScreen {
      +onDaySelected(day:Int):void
    }

    class HomeScreenViewModel {
      -weatherRepository:WeatherRepository
      +getActivitiesForAFutureDay(day:Int):void
    }

    class WeatherRepository {
      -locationForecastRepository:LocationForecastRepository
      -placesRepository:PlacesRepository
      -stravaRepository:StravaRepository
      -preferenceRepository:PreferencesRepository
      -aiRepository:AiRepository
      +getSuggestedActivitiesForOneDay(location:Location, day:Int):SuggestedActivities
      +replaceActivitiesForDay(day:Int, newActivities:SuggestedActivities):void
    }

    class LocationForecastRepository {
      +getTimeSeriesForDay(loc:Location, day:Int):Pair<List<TimeSeries>,Units?>
    }

    class PlacesRepository {
      +getNearbyPlaces(loc:Location):NearbyPlacesSuggestions
    }

    class StravaRepository {
      +getRouteSuggestions(loc:Location):RoutesSuggestions
    }

    class PreferencesRepository {
      +getEnabledPreferences():String
    }

    class AiRepository {
      -client:AiClient
+getSuggestionsForOneDay(forecastData, nearbyPlaces, routes, preferences, exclusion):SuggestedActivities
    }

    class AiClient {
        -ApiKey : String
        -BasePrompt : String
      +getSuggestionsForOneDay(forecastData, nearbyPlaces, routes, preferences, exclusion):String?
    }

    class PlacesClient {
        -ApiKey : String
    }
  ```