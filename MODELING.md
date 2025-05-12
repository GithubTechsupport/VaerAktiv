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
        wr[WeatherRepository]
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
        ss[SettingsScreen]
        ips[InfoPreferencesScreen]
        omv[OsmMapView]
    end

    %% === Connections ===
    sapi --> sds --> sr --> wr
    aiapi --> air --> wr
    gapi --> gc --> wr
    fapi --> dlds --> dlr --> wr
    json --> flds --> flr --> flvm --> ls
    gc --> flr
    goapi --> plr --> wr
    stapi --> stds --> str --> wr
    mapi --> mds --> mr --> wr
    lapi --> lfds --> lfr --> wr
    napi --> nds --> nr --> wr
    json --> pds --> pr --> wr
    pr --> pvm --> ss
    pvm --> ips
    jt --> ddtds --> ddtr --> hsvm
    wr --> hsvm --> hs
    wr --> flvm
    wr --> msvm --> ms
    oapi --> omv --> ms
```