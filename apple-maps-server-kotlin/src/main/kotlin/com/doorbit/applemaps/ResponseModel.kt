package com.doorbit.applemaps

data class PlacesResponse(
    val results: List<Place> = emptyList()
)

data class Place(
    val coordinate: Coordinate,
    val displayMapRegion: DisplayMapRegion,
    val name: String,
    val formattedAddressLines: List<String>,
    val structuredAddress: StructuredAddress,
    val country: String,
    val countryCode: String
)
data class Coordinate(
    val latitude: Double,
    val longitude: Double
)

data class DisplayMapRegion(
    val southLatitude: Double,
    val westLongitude: Double,
    val northLatitude: Double,
    val eastLongitude: Double
)

data class StructuredAddress(
    val locality: String,
    val postCode: String,
    val subLocality: String?,
    val thoroughfare: String,
    val subThoroughfare: String,
    val fullThoroughfare: String,
    val dependentLocalities: List<String>?
)