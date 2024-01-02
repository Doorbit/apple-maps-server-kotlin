package com.doorbit.applemaps

import java.net.URLEncoder

/**
 * Possible input for the /search and /searchAutocomplete API.
 *
 * @param q The search prompt.
 * @param excludePoiCategories A comma-separated list of POI categories to exclude from the results. For example: excludePoiCategories=Airport,Cafe.
 * @param includePoiCategories A comma-separated list of POI categories to include in the results. For example: includePoiCategories=Airport,Cafe.
 * @param language The language the server should use when returning the response, specified using a BCP 47 language code. For example (and default), for English use lang=en-US.
 * @param limitToCountries A comma-separated list of two-letter ISO 3166-1 codes to limit the results to. For example: limitToCountries=DE,NL. If you specify two or more countries, the results reflect the best available results for some or all of the countries rather than everything related to the query for those countries.
 * @param resultTypeFilter A comma-separated list of result types to filter the results to. For example: resultTypeFilter=Address,Poi.
 * @param searchLocation A location defined by the application as a hint.
 * @param searchRegion A bounding box defined by the application as a hint area in which to search for results.
 * @param userLocation The location of the user as a hint.
 */
data class SearchInput(
    val q: String,
    val excludePoiCategories: List<PoiCategory>? = null,
    val includePoiCategories: List<PoiCategory>? = null,
    val language: String? = null,
    val limitToCountries: List<String>? = null,
    val resultTypeFilter: List<ResultType>? = null,
    val searchLocation: LatLon? = null,
    val searchRegion: BoundingBox? = null,
    val userLocation: LatLon? = null,
) {

    fun toQueryString(): String {
        val params = mutableListOf<String>()
        params.add("?q=${URLEncoder.encode(q, Charsets.UTF_8)}")
        excludePoiCategories?.let { categories -> params.add("excludePoiCategories=${categories.joinToString(",") { it.apiValue }}") }
        includePoiCategories?.let { categories -> params.add("includePoiCategories=${categories.joinToString(",") { it.apiValue }}") }
        language?.let { params.add("lang=${URLEncoder.encode(it, Charsets.UTF_8)}") }
        limitToCountries?.let { countries -> params.add("limitToCountries=${countries.joinToString(",") { URLEncoder.encode(it, Charsets.UTF_8) }}") }
        resultTypeFilter?.let { resultTypes -> params.add("resultTypeFilter=${resultTypes.joinToString(",") { it.apiValue }}") }
        searchLocation?.let { params.add("searchLocation=${it.toQueryString()}") }
        searchRegion?.let { params.add("searchRegion=${it.toQueryString()}") }
        userLocation?.let { params.add("userLocation=${it.toQueryString()}") }
        return params.joinToString("&")
    }

}