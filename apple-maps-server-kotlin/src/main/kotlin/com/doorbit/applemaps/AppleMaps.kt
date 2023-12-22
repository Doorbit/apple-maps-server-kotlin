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

class AppleMaps(
    authToken: String,
    private val timeout: Duration,
) {
    constructor(authToken: String) : this(authToken, DEFAULT_TIMEOUT)

    private val objectMapper = defaultObjectMapper()
    private val authorizationService = AppleMapsAuthorizationService(objectMapper, API_SERVER, timeout, authToken)
    private val httpClient = HttpClient.newHttpClient()

    fun geocode(address: String): List<Place> {
        return geocode(GeocodeInput(address))
    }

    fun geocode(input: GeocodeInput): List<Place> {
        val uri = URI.create("$GEOCODING_URL${input.toQueryString()}")
        return invokeApi<PlacesResponse>(uri).results
    }

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
        private const val REVERSE_GEOCODING_URL = "$API_SERVER/v1/reverseGeocode"
        private val DEFAULT_TIMEOUT = Duration.ofSeconds(10)
    }

}


