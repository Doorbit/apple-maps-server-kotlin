package com.doorbit.applemaps

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.time.Instant
import java.util.*

class AppleMapsAuthorizationService(
    private val objectMapper: ObjectMapper,
    private val apiServer: String,
    private val timeout: Duration,
    private val authToken: String
) {

    @Volatile
    private var accessToken: AccessToken? = null
    private val httpClient = HttpClient.newHttpClient()

    @Synchronized
    fun getAccessToken(): String {
        if (!isExpired()) return accessToken!!.tokenString
        return refreshAccessToken().tokenString
    }

    private fun isExpired(): Boolean {
        if (accessToken == null) return true
        return accessToken!!.expiresAt.minusSeconds(ACCESS_TOKEN_GRACE_PERIOD_SECONDS).isAfter(Instant.now())
    }

    private fun refreshAccessToken(): AccessToken {
        val httpRequest = HttpRequest.newBuilder()
            .GET()
            .timeout(timeout)
            .uri(URI.create("$apiServer/v1/token"))
            .setHeader("Authorization", "Bearer $authToken")
            .build()
        return try {
            val response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray())

            if (response.statusCode() != 200) {
                throw RuntimeException("Received ${response.statusCode()} when invoking Apple Maps API: ${response.body().decodeToString()}")
            }

            val jsonNode = objectMapper.readValue(response.body(), JsonNode::class.java)
            val tokenString = jsonNode["accessToken"].asText()
            val expiresAt = extractExpiry(tokenString)

            this.accessToken = AccessToken(tokenString, expiresAt)
            accessToken!!
        } catch (e: Exception) {
            throw RuntimeException("Failed to exchange Apple Maps authorization token for an access token", e)
        }
    }

    private fun extractExpiry(tokenString: String): Instant {
        val tokenBodyEncoded: String = tokenString.split(".")[1]
        val tokenBody = Base64.getDecoder().decode(tokenBodyEncoded).toString(Charsets.UTF_8)
        val tokenBodyJsonNode = objectMapper.readValue(tokenBody, JsonNode::class.java)
        val exp = tokenBodyJsonNode["exp"].asLong()
        return Instant.ofEpochSecond(exp)
    }

    private data class AccessToken(val tokenString: String, val expiresAt: Instant)

    companion object {
        private const val ACCESS_TOKEN_GRACE_PERIOD_SECONDS = 30L
    }
}