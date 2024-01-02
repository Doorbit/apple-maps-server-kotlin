package com.doorbit.applemaps

/**
 * A viewbox that can be used as a hint for the geocoding service.
 * The viewbox is defined by 2 points. Lat/Lon of the upper right corner and Lat/Lon of the lower left corner of the viewbox.
 */
data class BoundingBox(
    val northLatitude: Double,
    val eastLongitude: Double,
    val southLatitude: Double,
    val westLongitude: Double
) {
    fun toQueryString(): String {
        return "$northLatitude,$eastLongitude,$southLatitude,$westLongitude"
    }

}