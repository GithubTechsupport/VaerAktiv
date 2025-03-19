package no.uio.ifi.in2000.EmptyApplication.model.metalerts

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class Geometry(
    val coordinates : JsonElement,
    val type : String
) {
    fun getCoordinatesAsList(): List<List<List<Float>>> {
        return when (type) {
            "Polygon" -> {
                coordinates.jsonArray.map { ring ->
                    ring.jsonArray.map { point ->
                        listOf(point.jsonArray[0].jsonPrimitive.float, point.jsonArray[1].jsonPrimitive.float)
                    }
                }
            }
            "MultiPolygon" -> {
                coordinates.jsonArray[0].jsonArray.map { ring ->
                    ring.jsonArray.map { point ->
                        listOf(point.jsonArray[0].jsonPrimitive.float, point.jsonArray[1].jsonPrimitive.float)
                    }
                }
            }
            else -> emptyList()
        }
    }
}