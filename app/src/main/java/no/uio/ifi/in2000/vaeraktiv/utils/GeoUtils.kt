package no.uio.ifi.in2000.vaeraktiv.utils

private const val NORWAY_MIN_LAT = 57.9
private const val NORWAY_MAX_LAT = 71.4
private const val NORWAY_MIN_LON = 4.0
private const val NORWAY_MAX_LON = 31.0

fun isInNorway(lat: Double, lon: Double): Boolean {
    return lat in NORWAY_MIN_LAT..NORWAY_MAX_LAT &&
            lon in NORWAY_MIN_LON..NORWAY_MAX_LON
}