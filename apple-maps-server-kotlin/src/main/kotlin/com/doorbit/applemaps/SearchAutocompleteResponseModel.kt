package com.doorbit.applemaps

data class SearchAutocompleteResponse(
    val results: List<AutocompleteResult> = emptyList()
)

/**
 * A result from the autocomplete API.
 * Each result contains a completion URL that can be used to get more information about the result (Such as the full address)
 * @param completionUrl The URL that can be used to get more information about the result. Access token must be provided anyway.
 * @param displayLines A list of formatted address lines for the autocomplete result.
 * @param location The location of the autocomplete result.
 * @param structuredAddress A structured address object for the autocomplete result. This object has shown to be null in almost all cases. Hence, to retreive the full
 * address, you need to call the completion URL.
 * @param places A list of places that belong to the autocomplete result. Only present if followCompletionUrls is true.
 */
data class AutocompleteResult(
    val completionUrl: String,
    val displayLines: List<String>,
    val location: Coordinate,
    val structuredAddress: StructuredAddress? = null,
    val places: List<Place>? = null
) {
    fun withPlaces(places: List<Place>) : AutocompleteResult {
        return this.copy(places = places)
    }
}