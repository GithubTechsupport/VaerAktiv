package no.uio.ifi.in2000.vaeraktiv.model.metalerts

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

/**
 * Geometry data for a met alerts feature, holding raw JSON coordinates.
 *
 * @param coordinates JSON element representing polygon or multipolygon coords
 * @param type the GeoJSON geometry type ("Polygon" or "MultiPolygon")
 */
@Serializable
data class Geometry(
    val coordinates : JsonElement,
    val type : String
) {
    /**
     * Parses raw JSON coordinates into a nested list of floats.
     *
     * @return List of rings each containing points [longitude, latitude]
     */
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