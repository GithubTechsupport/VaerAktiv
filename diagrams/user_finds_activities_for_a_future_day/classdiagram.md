  ---
title: Aktivitetsdiagram - Finn aktiviteter for en fremtidig dag
---
  ```mermaid
  classDiagram
      WeatherWeekUI "1" o-- "1" HomeScreenViewModel : viewModel
      HomeScreenViewModel "1" -- "1" IWeatherRepository : weatherRepo
      IWeatherRepository <|-- WeatherRepository
      WeatherRepository "1" -- "1" LocationForecastRepository : locationForecastRepo
      WeatherRepository "1" -- "1" PlacesRepository : placesRepo
      WeatherRepository "1" -- "1" StravaRepository : stravaRepo
      WeatherRepository "1" -- "1" PreferenceRepository : preferenceRepo
      WeatherRepository ..> AiRepository : aiRepo

      class WeatherWeekUI {
        
      }

      class HomeScreenViewModel {
        +getActivitiesForAFutureDay(dayNr:Int):void
      }

      class IWeatherRepository {
        <<interface>>
        +getSuggestedActivitiesForOneDay(location:Location, dayNr:Int):SuggestedActivities
        +replaceActivitiesForDay(dayNr:Int, newActivities:SuggestedActivities):void
      }

      class WeatherRepository {
        +getSuggestedActivitiesForOneDay(location:Location, dayNr:Int):SuggestedActivities
        +replaceActivitiesForDay(dayNr:Int, newActivities:SuggestedActivities):void
      }

      class LocationForecastRepository {
        +getForecastByDay(lat:String, lon:String):Pair\<List\<Pair\<LocalDate, List\<TimeSeries\>\>\>, Units\>
      }

      class PlacesRepository {
        +getNearbyPlaces(location:LatLng):NearbyPlacesSuggestions
      }

      class StravaRepository {
        +getRouteSuggestions(location:Location):RoutesSuggestions
      }

      class PreferenceRepository {
        +getEnabledPreferences():String
      }

      class AiRepository {
        +getSuggestionsForOneDay(prompt:FormattedForecastDataForPrompt, nearbyPlaces:NearbyPlacesSuggestions, routes:RoutesSuggestions, preferences:String, exclusion:String):SuggestedActivities
      }
  ```