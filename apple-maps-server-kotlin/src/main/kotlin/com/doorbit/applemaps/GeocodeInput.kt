package com.doorbit.applemaps

import java.net.URLEncoder

/**
 * Possible input for the geocoding API.
 * @param address The address String to geocode.
 * @param limitToCountries A comma-separated list of two-letter ISO 3166-1 codes to limit the results to. For example: limitToCountries=DE,NL. If you specify two or more countries, the results reflect the best available results for some or all of the countries rather than everything related to the query for those countries.
 * @param language The language the server should use when returning the response, specified using a BCP 47 language code. For example (and default), for English use lang=en-US.
 * @param searchLocation A location defined by the application as a hint.
 * @param searchRegion A bounding box defined by the application as a hint area in which to search for results.
 * @param userLocation The location of the user as a hint.
 */
data class GeocodeInput(
    val address: String,
    val limitToCountries: List<String>? = null,
    val language: String? = null,
    val searchLocation: LatLon? = null,
    val searchRegion: BoundingBox? = null,
    val userLocation: LatLon? = null,
) {

    fun toQueryString(): String {
        val params = mutableListOf<String>()
        params.add("?q=${URLEncoder.encode(address, Charsets.UTF_8)}")
        limitToCountries?.let { countries -> params.add("limitToCountries=${countries.joinToString(",") { URLEncoder.encode(it, Charsets.UTF_8) }}") }
        language?.let { params.add("lang=${URLEncoder.encode(it, Charsets.UTF_8)}") }
        searchLocation?.let { params.add("searchLocation=${it.toQueryString()}") }
        searchRegion?.let { params.add("searchRegion=${it.toQueryString()}") }
        userLocation?.let { params.add("userLocation=${it.toQueryString()}") }
        return params.joinToString("&")
    }

}