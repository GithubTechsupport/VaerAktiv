```mermaid
sequenceDiagram
    actor User
    participant UI as ActivityCard
    participant HomeVM as HomeScreenViewModel
    participant Nav as Navbar
    participant MS as MapScreen
    participant MapVM as MapScreenViewModel
    participant Osm as OsmMapView

    User->>UI: click "View in Map"
    UI->>HomeVM: viewActivityInMap(activity)
    HomeVM->>HomeVM: launch coroutine & emit navigateToMap(activity)
    HomeVM-->>Nav: navigateToMap(activity)
    Nav->>Nav: navigateToMap()\npop to map route
    Nav->>MS: onNavigate(activity)
    MS->>MapVM: zoomInOnActivity(activity)
    alt success
        alt activity is PlaceActivitySuggestion
            MapVM->>MapVM: points = listOf(activity.coordinates)
        else StravaActivitySuggestion
            MapVM->>MapVM: points = decodePolyline(activity.polyline)
        end
        MapVM-->>MS: updateUiState(selectedActivityPoints)
        MS-->>Osm: update(..., selectedActivityPoints)
        Osm-->>MS: onZoomHandled()
        MS->>MapVM: clearSelectedActivityPoints()
        MS-->>User: map centered on activity
    else error
        MapVM-->>MS: errorOccurred(error)
        MS-->>UI: showErrorDialog(error)
    end
```