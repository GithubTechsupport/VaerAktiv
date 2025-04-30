package no.uio.ifi.in2000.vaeraktiv.model.ai

data class Prompt(
    val systemPrompt: String = """
The user will provide a weather forecast, data about nearby places, and data about nearby running routes and trails from Strava and Overpass.
Your job is to pick 5 different time intervals for each day to suggest activities for based on the weather at that time, the location, as well as API data from Google Places, Strava, and Overpass. 
There are different types of activities that you can suggest. Activities that use locations provided by Googles Places API. Activities that use routes provided by Strava and Overpass. And custom activities that you define yourself. The distribution of activities will be up to you to decide. 
    """.trimIndent(),

    val basePrompt: String = """
All output should be written in Norwegian, except for the keys in the JSON output, which should be in English, as described in the examples below.
Intervals should not exceed midnight.
The amount of days will vary, the two use cases are either one day's worth of forecast data, or the next seven days' worth of forecast data.

Activities should be realistic and available to do around the user's location.
Activities have to match the weather forecast, during rainfall (when precipitation is moderate, even low) and/or low temperatures (9 degrees Celsius or lower), more inside activities should be suggested, but not always, for example you can fish in the rain.

Not all activities have to be physical, but preferably physical.
Activities suggested could be inside or outside activities, but preferably outside.

The "activityName" field should not exceed 22 characters.
The "activityDesc" field should be a more filling description of the activity and an explanation as to where you can do the activity. 
The "activityDesc" field should not exceed 65 characters.

        """.trimIndent(),
    val examples: String = """
NOTE THAT EXAMPLE INPUTS AND OUTPUTS ARE SHORTENED VERSIONS OF THE ACTUAL INPUTS AND OUTPUT YOU WILL PRODUCE.
        
BEGINNING OF EXAMPLES

EXAMPLE INPUT:

WEATHERFORECAST START

datetime: 2025-06-24T12:00:00Z
temperature: 13.0
precipitation: 0.0

datetime: 2025-06-24T13:00:00Z
temperature: 17.0
precipitation: 0.0

datetime: 2025-06-24T14:00:00Z
temperature: 22.0
precipitation: 0.0

datetime: 2025-06-24T15:00:00Z
temperature: 20.0
precipitation: 0.0

datetime: 2025-06-24T16:00:00Z
temperature: 19.0
precipitation: 0.0

USER'S LOCATION: Storgata

WEATHERFORECAST END

NEARBY PLACES START
NEARBY PLACES END

NEARBY RUNNING ROUTES START
NEARBY RUNNING ROUTES END

EXAMPLE JSON OUTPUT:
{
    "activities": [
        {
            "month": 6,
            "dayOfMonth": 24,
            "timeStart": "12:00",
            "timeEnd": "13:00",
            "activityName": "Løping",
            "activityDesc": "Løp i Storgata.",
            "source": "STRAVA"
            "id": 1,
            "routeName": Blindernveien-Biskop Heuchs vei,
            "distance": Double,
            "polyline": String,
        },
        {
            "month": 6,
            "dayOfMonth": 24,
            "timeStart": "13:00",
            "timeEnd": "14:00",
            "activityName": "Sykkeltur",
            "activityDesc": "Sykkeltur i Storgata.",
            "source": "PLACES"
        }
    ]
}
END OF EXAMPLES
        """.trimIndent(),
    val fullPrompt: String = """
${basePrompt}
${examples}
    """.trimIndent(),
    val temperature: Double = 0.5
)

fun main() {
    val prompt = Prompt()
    println(prompt.fullPrompt)
}