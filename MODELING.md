# Dataflowchart

```mermaid
flowchart BT
    %% === API-er ===
    subgraph APIs
        sapi((Sunrise API))
        aiapi((OpenAI))
        gapi((Android Geocoder))
        mapi((Met Alerts))
        lapi((Location Forecast))
        napi((Nowcast))
        oapi((OpenStreetMap))
        goapi((Google Places))
        fapi((FusedLocationProvider))
        stapi((Strava))
        json((JSON))
        jt((Java Time))
    end

    %% === Datasources ===
    subgraph DataSources
        sds[SunriseDataSource]
        air[AiRepository]
        gc[GeocoderClass]
        mds[MetAlertsDataSource]
        lfds[LocationForecastDataSource]
        nds[NowcastDataSource]
        dlds[DeviceLocationDataSource]
        stds[StravaDataSource]
        flds[FavoriteLocationDataSource]
        ddtds[DeviceDateTimeDataSource]
        pds[PreferenceDataSource]
    end

    %% === Repositories ===
    subgraph Repositories
        sr[SunriseRepository]
        ar[AggregateRepository]
        dlr[DeviceLocationRepository]
        flr[FavoriteLocationRepository]
        plr[PlacesRepository]
        str[StravaRepository]
        mr[MetAlertsRepository]
        lfr[LocationForecastRepository]
        nr[NowcastRepository]
        pr[PreferenceRepository]
        ddtr[DeviceDateTimeRepository]
    end

    %% === ViewModels og Skjermer ===
    subgraph ViewModels
        hsvm[HomeScreenViewModel]
        flvm[FavoriteLocationViewModel]
        msvm[MapScreenViewModel]
        pvm[PreferencesViewModel]
    end

    subgraph Screens
        hs[HomeScreen]
        ls[LocationScreen]
        ms[MapScreen]
        ss[PreferencesScreen]
        ips[InfoPreferencesScreen]
        omv[OsmMapView]
    end

    %% === Connections ===
    sapi --> sds --> sr --> ar
    aiapi --> air --> ar
    gapi --> gc --> ar
    fapi --> dlds --> dlr --> ar
    json --> flds --> flr --> flvm --> ls
    gc --> flr
    goapi --> plr --> ar
    stapi --> stds --> str --> ar
    mapi --> mds --> mr --> ar
    lapi --> lfds --> lfr --> ar
    napi --> nds --> nr --> ar
    json --> pds --> pr --> ar
    pr --> pvm --> ss
    pvm --> ips
    jt --> ddtds --> ddtr --> hsvm
    ar --> hsvm --> hs
    ar --> flvm
    ar --> msvm --> ms
    oapi --> omv --> ms
```

# Use cases

## Add Favorite Location

**Name:** Add Favorite Location      
**Actors:** User

**Preconditions:**
- App is open on the favorite locations screen.

**Postconditions:**
- A new location is saved to persistent storage (file)
- Updated favorites list with the new location is displayed

**Main flow:**
1. User taps the **Add** icon.
2. System displays a search field.
3. User enters search text.
4. System calls the Places API for autocomplete suggestions.
5. System displays the list of suggestions.
6. User selects one suggestion.
7. System calls Geocoder to obtain coordinates for the selected place.
8. System saves the place name and coordinates to persistent storage.
9. System updates and displays the favorites list.

**Alternative flow – Empty Search Text:**

3.1. User enters an empty string.
3.2. System clears any existing suggestions.

**Alternative flow – No Suggestions Found:** 

3.4. User enters search text.      
3.5. System calls the Places API and receives no matches.      
3.6. System displays “No results.”

**Alternative flow – Location Already Saved:** 

6.8. User selects a suggestion.      
6.9. System detects the place already exists in favorites.      
6.10. System notifies the user, “Location already exists.”

[Activity Diagram](./diagrams/add_favorite_location/activitydiagram.md)

[Sequence Diagram](./diagrams/add_favorite_location/sequencediagram.md)

[Class Diagram](./diagrams/add_favorite_location/classdiagram.md)

## Find Activities for a Future Day

**Name:** Find Activities for a Future Day      
**Actors:** User

**Preconditions:**
- User is on the home screen with the weekly overview visible.

**Postconditions:**
- A list of generated suggested activities for the selected future day is displayed within the weekly overview.

**Main flow:**

1. User taps a day in the weekly overview.
2. System checks if activities are already loaded for that day.
3. If not loaded, system gathers weather forecast, nearby places, route options, and user preferences.
4. System sends the collected data to the AI service.
5. AI service returns a set of suggested activities.
6. System caches and displays the new activity list for the chosen day.

**Alternative flow – Existing Activities Present:**

1.1. User taps a day that already has loaded activities.      
1.2. System immediately displays the existing activity list.

**Alternative flow – Data Load Failure:**

5.1. System fails to retrieve data or receive AI suggestions.      
5.2. System displays an error message on the home screen.

[Activity Diagram](diagrams/find_activities_for_a_future_day/activitydiagram.md)

[Sequence Diagram](diagrams/find_activities_for_a_future_day/sequencediagram.md)

[Class Diagram](diagrams/find_activities_for_a_future_day/classdiagram.md)

## Refresh Activity

**Name:** Refresh Activity      
**Actors:** User

**Preconditions:**
- User is viewing activity cards for a specific day on the home screen.
- Activity card is not currently loading

**Postconditions:**
- The selected activity card is replaced with a newly generated suggested activity.

**Main flow:**
1. User taps the refresh icon on an activity card.
2. System displays a loading placeholder on that card.
3. System gathers current location, weather forecast, nearby places, route options, and user preferences.
4. System invokes the AI service for a single new activity suggestion.
5. AI service returns a new ActivitySuggestion.
6. System replaces the original card with the new suggestion.

**Alternative flow – Suggestion Failure:**

3.1. System fails to gather data.      
3.2. System restores the previous activity card state.      
3.3. System displays an error indicator on the card.

[Activity Diagram](diagrams/refresh_activity/activitydiagram.md)

[Sequence Diagram](diagrams/refresh_activity/sequencediagram.md)

[Class Diagram](diagrams/refresh_activity/classdiagram.md)

## View Activity in Map

**Name:** View Activity in Map      
**Actors:** User

**Preconditions:**
- User is viewing an activity card on the home screen.
- Activity for given activity card is not loading and is cached

**Postconditions:**
- The map screen is displayed, centered and zoomed on the selected activity coordinates.

**Main flow:**
1. User taps **View in Map** on an activity card.
2. System navigates to the map screen with the selected activity.

**Alternative flow – Single Location:**

2.1. System identifies activity as a single location.  
2.2. System retrieves its coordinates  
2.3. System places a marker at coordinate  
2.4. System updates the map view and centers camera on coordinate

**Alternative flow - Route:**  

2.1. System identifies activity as a route.  
2.2. System retrieves string encoded polyline  
2.3. System decodes polyline to get coordinates  
2.4. System draws route through coordinates  
2.5. System centers and zooms in on route coordinates

**Alternative flow – Decoding error:**

2.3.1. System fails to decode the polyline.      
2.3.2. System displays an error dialog instead of centering the map.

[Activity Diagram](./diagrams/view_activity_in_map/activitydiagram.md)

[Sequence Diagram](./diagrams/view_activity_in_map/sequencediagram.md)

[Class Diagram](./diagrams/view_activity_in_map/classdiagram.md)

**Use Case Diagram**

[Use_Case_Diagram](./diagrams/usecase/UsecaseDiagram.svg)
