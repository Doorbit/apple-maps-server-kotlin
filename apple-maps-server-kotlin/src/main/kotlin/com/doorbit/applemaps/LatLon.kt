package com.doorbit.applemaps


data class LatLon(val latitude: Double, val longitude: Double) {
    fun toQueryString(): String {
        return "$latitude,$longitude"
    }
}
