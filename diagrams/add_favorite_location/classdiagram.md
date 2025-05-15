```mermaid
classDiagram
    FavoriteLocationScreen "1" --o "1" FavoriteLocationViewModel : ViewModel
    FavoriteLocationViewModel "1" --o "1" FavoriteLocationRepository : Data Repository
    FavoriteLocationViewModel "1" --o "1" AggregateRepository : Dependency
    FavoriteLocationRepository "1" --o "1" GeocoderClass : Dependency
    FavoriteLocationRepository "1" --o "1" FavoriteLocationDataSource : Data Source
    PlacesClient "1" o-- "1" PlacesRepository : Dependency
    PlacesRepository "1" o-- "1" AggregateRepository : Dependency

    class AggregateRepository {
      -placesRepository: PlacesRepository
            +getAutocompletePredictions(query:String,token:AutocompleteSessionToken): List~AutocompletePrediction~
    }

    class PlacesClient {
        -ApiKey : String
    }

    class FavoriteLocationScreen {
      +onSearchInputChanged(query:String):void
      +onSuggestionClick(fullText:String):void
    }

    class FavoriteLocationViewModel {
      +favoriteLocations : MutableStateFlow~List~FavoriteLocation~
      -aggregateRepository: AggregateRepository
      -sessionToken:AutocompleteSessionToken?
      +predictions: StateFlow~List~AutocompletePrediction~~
      +fetchPredictions(query:String):void
      +addLocation(loc:String):void
    }

    class FavoriteLocationRepository {
      +addLocationByName(placeName:String):void
      +deleteLocationByName(placeName:String):void
      +getAllLocations():List~String~
    }

    class GeocoderClass {
      +getCoordinatesFromLocation(placeName:String):Pair~Double,Double~
    }

    class FavoriteLocationDataSource {
      -favoriteLocationsFile : File
      +addLocation(placeName:String,latitude:Double,longitude:Double):void
      +deleteLocation(placeName:String):void
      +getAllLocations():List~String~
    }

    class PlacesRepository {
      +getAutocompletePredictions(query:String,token:AutocompleteSessionToken):List~AutocompletePrediction~
    }

    class AutocompletePrediction
    class AutocompleteSessionToken
```