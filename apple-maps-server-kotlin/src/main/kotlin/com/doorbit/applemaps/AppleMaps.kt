package com.doorbit.applemaps

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration

/**
 * @param authToken The Apple Maps authorization token to use when invoking the Apple Maps API.
 * @param timeout The timeout to use when invoking the Apple Maps API.
 */
class AppleMaps(
    authToken: String,
    private val timeout: Duration,
) {
    /**
     * @param authToken The Apple Maps authorization token to use when invoking the Apple Maps API.
     */
    constructor(authToken: String) : this(authToken, DEFAULT_TIMEOUT)

    private val objectMapper = defaultObjectMapper()
    private val authorizationService = AppleMapsAuthorizationService(objectMapper, API_SERVER, timeout, authToken)
    private val httpClient = HttpClient.newHttpClient()

    /**
     * Geocodes the given input and returns a list of found places.
     * @param input The input parameters that will be sent to Apple Maps for geocoding.
     */
    fun geocode(input: GeocodeInput): List<Place> {
        val uri = URI.create("$GEOCODING_URL${input.toQueryString()}")
        return invokeApi<PlacesResponse>(uri).results
    }

    /**
     * Autocompletes the given input and returns a list of autocomplete results.
     * This can be a (partial) address or a POI name, for example.
     *
     * @param input The input parameters that will be sent to Apple Maps for autocompletion.
     * @param followCompletionUrls Calls the completion URLs for each autocomplete result in parallel and enriches the AutocompleteResult with Places information. Each resolved completion URL will consume your Service Request quota.
     */
    fun autocomplete(input: SearchInput, followCompletionUrls: Boolean = false): List<AutocompleteResult> {
        val uri = URI.create("$AUTOCOMPLETE_URL${input.toQueryString()}")

        val results = invokeApi<SearchAutocompleteResponse>(uri).results

        if (!followCompletionUrls) {
            return results
        }

        return results.parallelStream().map {
            it.withPlaces(resolveAutoCompletionUrl(it.completionUrl).results)
        }.toList()
    }

    /**
     * Searches for the given input and returns a search result.
     */
    fun search(input: SearchInput): SearchResponse {
        val uri = URI.create("$SEARCH_URL${input.toQueryString()}")
        return invokeApi<SearchResponse>(uri)
    }

    /**
     * Resolves the given completion URL and returns a SearchResponse.
     * @param completionUrl The completion URL to resolve, as returned by the autocomplete API.
     */
    fun resolveAutoCompletionUrl(completionUrl: String): SearchResponse {
        val uri = URI.create("$API_SERVER$completionUrl")
        return invokeApi<SearchResponse>(uri)
    }

    /**
     * Reverse geocodes the given latitude and longitude and returns a list of found places.
     * @param latitude Latitude of a geographic point
     * @param longitude Longitude of a geographic point
     * @param language The language to use for the response, specified using a BCP 47 language code. Defaults to "en-US".
     */
    fun reverseGeocode(latitude: Double, longitude: Double, language: String = "en-US"): List<Place> {
        val uri = URI.create("$REVERSE_GEOCODING_URL?loc=$latitude,$longitude&lang=$language")
        return invokeApi<PlacesResponse>(uri).results
    }

    private inline fun <reified T> invokeApi(uri: URI): T {
        val httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(uri)
            .timeout(timeout)
            .setHeader("Authorization", "Bearer ${authorizationService.getAccessToken()}")
            .build()

        return try {
            val response = httpClient.send(httpRequest, BodyHandlers.ofInputStream())

            if (response.statusCode() != 200) {
                throw RuntimeException("Received ${response.statusCode()} when invoking Apple Maps API: ${response.body().readBytes().decodeToString()}")
            }

            objectMapper.readValue(response.body(), object : TypeReference<T>() {})
        } catch (e: Exception) {
            throw RuntimeException("Apple Maps Geocoding failed", e)
        }
    }

    private fun defaultObjectMapper(): ObjectMapper {
        val om = jacksonObjectMapper()
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return om
    }

    companion object {
        private const val API_SERVER = "https://maps-api.apple.com"
        private const val GEOCODING_URL = "$API_SERVER/v1/geocode"
        private const val SEARCH_URL = "$API_SERVER/v1/search"
        private const val AUTOCOMPLETE_URL = "$API_SERVER/v1/searchAutocomplete"
        private const val REVERSE_GEOCODING_URL = "$API_SERVER/v1/reverseGeocode"
        private val DEFAULT_TIMEOUT = Duration.ofSeconds(10)
    }

}


