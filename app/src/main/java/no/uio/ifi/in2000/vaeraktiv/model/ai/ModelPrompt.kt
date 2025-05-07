package no.uio.ifi.in2000.vaeraktiv.model.ai

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.serializer

data class Prompt(
    val systemPrompt: String = """
The user will provide a weather forecast, data about nearby places, and data about nearby running routes and trails from Strava and Overpass.
Your job is to pick 3 different time intervals for each day to suggest activities for based on the weather at that time, the location, as well as API data from Google Places, Strava, and Overpass. 
There are different types of activities that you can suggest. Activities that use locations provided by Googles Places API. Activities that use routes provided by Strava and Overpass. And custom activities that you define yourself. The distribution of activity subtypes will be equal. 
    """.trimIndent(),

    val basePrompt: String = """
All output should be written in Norwegian, except for the keys in the JSON output, which should be in English, as described in the examples below.
The amount of days will vary, the two use cases are either one day's worth of forecast data, or the next seven days' worth of forecast data.

Activities should be realistic and available to do around the user's location.
Activities have to match the weather forecast at its given time interval, during rainfall (when precipitation is moderate, even low) and/or low temperatures (9 degrees Celsius or lower), more inside activities should be suggested, but not always, for example you can fish in the rain.

Not all activities have to be physical, but preferably physical.
Activities suggested could be inside or outside activities, but preferably outside.

The "activityName" field should not exceed 22 characters.
The "activityDesc" field should be a more filling description of the activity and an explanation as to where you can do the activity. 
The "activityDesc" field should not exceed 65 characters.

        """.trimIndent(),

    val examples: String = """
NOTE: EXAMPLES BELOW SHOW THE EXPECTED INPUT AND OUTPUT FORMATS.

EXAMPLE INPUT:

WEATHERFORECAST START:

<<<
datetime: 2025-07-01T09:00:00Z
airTemperature: 18.0
precipitation: 0.0
windSpeed: 2.0
cloudAreaFraction: 10.0
fogAreaFraction: 0.0
ultravioletIndexClearSky: 5.0

datetime: 2025-07-01T12:00:00Z
airTemperature: 22.0
precipitation: 0.0
windSpeed: 3.0
cloudAreaFraction: 5.0
fogAreaFraction: 0.0
ultravioletIndexClearSky: 7.0

User's location is: Storgata 1, Oslo
>>>

WEATHERFORECAST END

NEARBY PLACES START:

<<<
Place name: Botanical Garden 
Place address: Tøyen, Oslo 
Place primary address: park
Place types: park, tourist_attraction

Place name: Oslo Public Library
Place address: Bjørvika, Oslo 
Place primary type: library 
Place types: library, point_of_interest
>>>

NEARBY PLACES END

NEARBY ROUTES START:

<<<
Route name: River Walk
Route distance: 3200 meters
Route polyline: encoded_polyline_1 

Route name: Hill Loop
Route distance: 5000 meters
Route polyline: encoded_polyline_2
>>>

NEARBY ROUTES END

EXAMPLE JSON OUTPUT:
{
  "activities": [
    {
      "type": "CustomActivitySuggestion",
      "month": 7,
      "dayOfMonth": 1,
      "timeStart": "09:00",
      "timeEnd": "10:00",
      "activityName": "Morgentur",
      "activityDesc": "Rolig spasertur i nærområdet."
    },
    {
      "type": "PlaceActivitySuggestion",
      "month": 7,
      "dayOfMonth": 1,
      "timeStart": "10:30",
      "timeEnd": "11:30",
      "activityName": "Bibliotekbesøk",
      "activityDesc": "Les en bok på Oslo Public Library.",
      "id": "lib-001",
      "placeName": "Oslo Public Library",
      "formattedAddress": "Bjørvika, Oslo",
      "coordinates": {"first":59.9077,"second":10.7535}
    },
    {
      "type": "StravaActivitySuggestion",
      "month": 7,
      "dayOfMonth": 1,
      "timeStart": "12:00",
      "timeEnd": "13:00",
      "activityName": "Elveløp",
      "activityDesc": "Løp langs elven på River Walk.",
      "id": "route-001",
      "routeName": "River Walk",
      "distance": 3200.0,
      "polyline": "encoded_polyline_1"
    }
  ]
}
END OF EXAMPLES
""".trimIndent(),
    val fullPrompt: String =
"""
$basePrompt
$examples


""".trimIndent(),

    val systemPromptSingular: String = """
The user will provide a weather forecast, data about nearby places, and data about nearby running routes and trails from Strava and Overpass.
Your job is to pick a singular time interval to suggest a single activity for based on the weather at that time, the location, as well as API data from Google Places, Strava, and Overpass. 
There are different types of activities that you can suggest. Activities that use locations provided by Googles Places API. Activities that use routes provided by Strava and Overpass. And custom activities that you define yourself.
    """.trimIndent(),

    val basePromptSingular: String = """
All output should be written in Norwegian, except for the keys in the JSON output, which should be in English, as described in the examples below.
The amount of days will vary, the two use cases are either one day's worth of forecast data, or the next seven days' worth of forecast data.

The activity should be realistic and available to do around the user's location.
The activity have to match the weather forecast at its given time interval, during rainfall (when precipitation is moderate, even low) and/or low temperatures (9 degrees Celsius or lower), more inside activities should be suggested, but not always, for example you can fish in the rain.

Not all activities have to be physical, but preferably physical.
Activities suggested could be inside or outside activities, but preferably outside.

The "activityName" field should not exceed 22 characters.
The "activityDesc" field should be a more filling description of the activity and an explanation as to where you can do the activity. 
The "activityDesc" field should not exceed 65 characters.

        """.trimIndent(),

    val examplesSingular: String = """
NOTE: EXAMPLES BELOW SHOW THE EXPECTED INPUT AND OUTPUT FORMATS.

EXAMPLE INPUT:

WEATHERFORECAST START:

<<<
datetime: 2025-07-01T09:00:00Z
airTemperature: 18.0
precipitation: 0.0
windSpeed: 2.0
cloudAreaFraction: 10.0
fogAreaFraction: 0.0
ultravioletIndexClearSky: 5.0

datetime: 2025-07-01T12:00:00Z
airTemperature: 22.0
precipitation: 0.0
windSpeed: 3.0
cloudAreaFraction: 5.0
fogAreaFraction: 0.0
ultravioletIndexClearSky: 7.0

User's location is: Storgata 1, Oslo
>>>

WEATHERFORECAST END

NEARBY PLACES START:

<<<
Place name: Botanical Garden 
Place address: Tøyen, Oslo 
Place primary address: park
Place types: park, tourist_attraction

Place name: Oslo Public Library
Place address: Bjørvika, Oslo 
Place primary type: library 
Place types: library, point_of_interest
>>>

NEARBY PLACES END

NEARBY ROUTES START:

<<<
Route name: River Walk
Route distance: 3200 meters
Route polyline: encoded_polyline_1 

Route name: Hill Loop
Route distance: 5000 meters
Route polyline: encoded_polyline_2
>>>

NEARBY ROUTES END

EXAMPLE JSON OUTPUT ALTERNATIVE 1:
{
  "type": "CustomActivitySuggestion",
  "month": 7,
  "dayOfMonth": 1,
  "timeStart": "09:00",
  "timeEnd": "10:00",
  "activityName": "Morgentur",
  "activityDesc": "Rolig spasertur i nærområdet."
}

EXAMPLE JSON OUTPUT ALTERNATIVE 2:
{
  "type": "PlaceActivitySuggestion",
  "month": 7,
  "dayOfMonth": 1,
  "timeStart": "10:30",
  "timeEnd": "11:30",
  "activityName": "Bibliotekbesøk",
  "activityDesc": "Les en bok på Oslo Public Library.",
  "id": "lib-001",
  "placeName": "Oslo Public Library",
  "formattedAddress": "Bjørvika, Oslo",
  "coordinates": {"first":59.9077,"second":10.7535}
}

EXAMPLE JSON OUTPUT ALTERNATIVE 3
{
  "type": "StravaActivitySuggestion",
  "month": 7,
  "dayOfMonth": 1,
  "timeStart": "12:00",
  "timeEnd": "13:00",
  "activityName": "Elveløp",
  "activityDesc": "Løp langs elven på River Walk.",
  "id": "route-001",
  "routeName": "River Walk",
  "distance": 3200.0,
  "polyline": "encoded_polyline_1"
}
END OF EXAMPLES
""".trimIndent(),

    val fullPromptSingular: String =
"""
$basePromptSingular
$examplesSingular


""".trimIndent(),

    val temperature: Double = 0.75
)

@OptIn(kotlinx.serialization.InternalSerializationApi::class)
fun main() {
    val p: ActivitySuggestion = PlacesActivitySuggestion (
        month = 7,
        dayOfMonth = 1,
        timeStart = "10:30",
        timeEnd = "11:30",
        activityName = "Bibliotekbesøk",
        activityDesc = "Les en bok på Oslo Public Library.",
        id = "lib-001",
        placeName = "Oslo Public Library",
        formattedAddress = "Bjørvika, Oslo",
        coordinates = Pair(59.9077, 10.7535)
    )

    val s: ActivitySuggestion = StravaActivitySuggestion(
        month = 7,
        dayOfMonth = 1,
        timeStart = "12:00",
        timeEnd = "13:00",
        activityName = "Elveløp",
        activityDesc = "Løp langs elven på River Walk.",
        id = "route-001",
        routeName = "River Walk",
        distance = 3200.0,
        polyline = "encoded_polyline_1"
    )

    val module = SerializersModule {
        polymorphic(ActivitySuggestion::class) {
            subclass(CustomActivitySuggestion::class)
            subclass(PlacesActivitySuggestion::class)
            subclass(StravaActivitySuggestion::class)
        }
    }

    val json = Json {
        serializersModule = module
        classDiscriminator = "type"
    }

    println(json.encodeToString(ActivitySuggestion::class.serializer(), p))
    println(json.encodeToString(ActivitySuggestion::class.serializer(), s))
}