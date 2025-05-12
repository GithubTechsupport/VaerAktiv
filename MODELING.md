■ Beskrivelse og diagrammer, vi anbefaler å generere dem med
Mermaid som vist på forelesning. Se kravene til modellering
lenger ned i dette dokumentet. Ha med hvorfor diagrammet er
valgt og hva dere ønsker å med det.

Modellering og systemdesign
● De viktigste funksjonelle kravene til applikasjonen bør beskrives – bruk gjerne
use case diagram, samt sekvensdiagram og tekstlig beskrivelse av de
viktigste use-casene.
● Modelleringen bør også inneholde klassediagram som reflekterer use-case og
sekvensdiagrammene.
● Andre diagrammer bør også være inkludert for å få frem andre perspektiver,
for eksempel aktivitetsdiagram (flytdiagram) eller tilstandsdiagram.

```mermaid
flowchart BT
    %% === API-er ===
    sapi((sunrise))
    aiapi((openAI))
    gapi((geocoder))
    mapi((metAlerts))
    lapi((locationForecast))
    napi((nowcast))
    oapi((openStreetMap))

    %% === Datasources ===
    sapi --> sds[SunriseDataSource]
    aiapi --> air[AiRepository]
    gapi --> gc[GeocoderClass]
    mapi --> mds[MetAlertsDataSource]
    lapi --> lfds[LocationForecastDataSource]
    napi --> nds[NowcastDataSource]

    ddtds[DeviceDateTimeDataSource]
    dlds[DeviceLocationDataSource]
    flds[FavoriteLocationDataSource]
    stds[StravaDataSource]
    sam[StravaAuthManager] --> stds
    pds[PreferenceDataSource]

    %% === Repositories ===
    sds --> sr[SunriseRepository]
    sr --> wr[WeatherRepository]
    air --> wr
    gc --> wr
    dlds --> dlr[DeviceLocationRepository] --> wr
    flds --> flr[FavoriteLocationRepository]
    flr --> flvm[FavoriteLocationViewModel]
    gc --> flr
    plr[PlacesRepository] --> wr
    stds --> str[StravaRepository] --> wr
    mds --> mr[MetAlertsRepository] --> wr
    lfds --> lfr[LocationForecastRepository] --> wr
    nds --> nr[NowcastRepository] --> wr
    pds --> pr[PreferenceRepository]
    pr --> wr

    %% === ViewModels og Skjermer ===
    pr --> pvm[PreferencesViewModel]
    pvm --> ss[SettingsScreen]
    pvm --> ips[InfoPreferencesScreen]

    ddtds --> ddtr[DeviceDateTimeRepository] --> hsvm[HomeScreenViewModel]
    wr --> hsvm
    hsvm --> hs[HomeScreen]

    wr --> flvm --> ls[LocationScreen]
    wr --> msvm[MapScreenViewModel] --> ms[MapScreen]
    oapi --> omv[OsmMapView] --> ms
```