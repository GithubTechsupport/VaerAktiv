package no.uio.ifi.in2000.vaeraktiv.model.ai

data class Prompt(
    val systemPrompt: String = """
The user will provide a weather forecast.
Your job is to pick 3 different time intervals for each day to suggest activities for based on the weather at that time, the location and other requirements defined in the user prompt.
    """.trimIndent(),

    val basePrompt: String = """
All output should be written in Norwegian, except for the keys in the JSON output, which should be in English, as described in the examples below.
Based on the weather forecast and the users location, pick 3 different time intervals for every single day in the forecast data to suggest activities.
The amount of days will vary, the two use cases are either one day's worth of forecast data, or the next seven days' worth of forecast data.

Activities should be realistic and available to do around the user's location.
Activities has to match the weather forecast, during rainfall (when precipitation is moderate, even low) and/or low temperatures (9 degrees Celsius or lower), more inside activities should be suggested, but not always, for example you can fish in the rain.

Within the "activityName" field should briefly explain the activity
Within the "activityDesc" field should be a more filling description of the activity and an explanation as to where you can do the activity.

Not all activities have to be physical, but preferably physical.
Activities suggested could be inside or outside activities.

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

datetime: 2025-06-25T12:00:00Z
temperature: 9.0
precipitation: 0.0

datetime: 2025-06-25T13:00:00Z
temperature: 10.0
precipitation: 0.0

datetime: 2025-06-25T14:00:00Z
temperature: 12.0
precipitation: 0.0

datetime: 2025-06-25T15:00:00Z
temperature: 10.0
precipitation: 0.0

datetime: 2025-06-25T16:00:00Z
temperature: 10.0
precipitation: 0.0

WEATHERFORECAST END

USER'S LOCATION: Storgata

EXAMPLE JSON OUTPUT:
{
    "activities": [
        {
            "month":"6",
            "dayOfMonth":"24",
            "timeStart":"14:00",
            "timeEnd":"16:00",
            "activityName":"Svømming ved Badedammen Grorud"
            "activityDesc":"Denne dagen er det varmt og mye sol. God dag for svømming ved badedammen ved Grorud."
        }
        {
            "month":"6",
            "dayOfMonth":"25",
            "timeStart":"11:00",
            "timeEnd":"13:00",
            "activityName":"Bowling på Veitvet senter"
            "activityDesc":"Denne dagen er det kjørligere, og da kan du nyte en innendørs aktivitet på Veitvet senter."
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