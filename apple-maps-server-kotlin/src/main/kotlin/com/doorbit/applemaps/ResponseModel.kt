package com.doorbit.applemaps

data class PlacesResponse(
    val results: List<Place> = emptyList()
)

/**
 * A place is a search result returned by the geocoding API.
 *
 * @param coordinate Coordinate point (latitude/longitude) of the place.
 * @param displayMapRegion A viewbox that can be used to display the place on a map. It shows the place itself and its surrounding area. The viewbox is defined by 2 points. Lat/Lon of the upper right corner and Lat/Lon of the lower left corner.
 * @param name A human readable displayname of the place.
 * @param formattedAddressLines A list of formatted address lines for the place.
 * @param structuredAddress A structured address object for the place. Depending on the amount of information given in the address prompt, the structured address object may contain more or less information.
 * @param country The country of the place.
 * @param countryCode The country code of the place, e.g. "DE" or "US".
 */
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

/**
 * A viewbox that can be used to display the place on a map. It shows the place itself and its surrounding area.
 * The viewbox is defined by 2 points. Lat/Lon of the upper right corner and Lat/Lon of the lower left corner of the viewbox.
 */
data class DisplayMapRegion(
    val northLatitude: Double,
    val eastLongitude: Double,
    val southLatitude: Double,
    val westLongitude: Double
)

/**
 * A structured address object for the place. Depending on the amount of information given in the address prompt,
 * the structured address object may contain more or less information.
 *
 * @param locality Typically the city/town/village of the place, e.g. "Hamburg" or "San Francisco".
 * @param postCode The postal code of the place, e.g. "20354" or "94105".
 * @param subLocality Typically the district of the place, e.g. "Hamburg-Altstadt" or "The East Cut".
 * @param thoroughfare Typically the street name of the place, e.g. "Jungfernstieg" or "Main St".
 * @param subThoroughfare Typically the house number of the place, e.g. "1b" or "100".
 * @param fullThoroughfare Typically the full street address of the place, e.g. "Jungfernstieg 1b" or "100 Main St".
 * @param dependentLocalities Typically the dependent localities of the place, e.g. ["Hamburg-Altstadt", "Hamburg-St. Georg" or ["The East Cut", "South Beach"].
 */
data class StructuredAddress(
    val locality: String?,
    val postCode: String?,
    val subLocality: String?,
    val thoroughfare: String?,
    val subThoroughfare: String?,
    val fullThoroughfare: String?,
    val dependentLocalities: List<String>?
)