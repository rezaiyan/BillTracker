package com.example.client.api

import com.example.client.model.Subscription
import com.example.client.model.SubscriptionFrequency
import com.example.client.util.BigDecimalSerializer
import com.example.client.util.InstantSerializer
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import mu.KotlinLogging
import java.math.BigDecimal
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * API client for interacting with the Subscription Manager backend
 */
class SubscriptionApiClient(
    private val baseUrl: String = "http://10.0.2.2:3000/api/subscriptions" // Android emulator localhost
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                serializersModule = SerializersModule {
                    contextual(BigDecimal::class, BigDecimalSerializer)
                    contextual(Instant::class, InstantSerializer)
                }
            })
        }
    }

    /**
     * Get all subscriptions
     */
    suspend fun getAllSubscriptions(): List<Subscription> = withContext(Dispatchers.IO) {
        try {
            logger.info { "Fetching all subscriptions" }
            client.get(baseUrl).body()
        } catch (e: Exception) {
            logger.error(e) { "Error fetching subscriptions" }
            emptyList()
        }
    }

    /**
     * Get active subscriptions
     */
    suspend fun getActiveSubscriptions(): List<Subscription> = withContext(Dispatchers.IO) {
        try {
            logger.info { "Fetching active subscriptions" }
            val response: HttpResponse = client.get("$baseUrl/active")
            response.body<List<Subscription>>()
        } catch (e: Exception) {
            logger.error(e) { "Error fetching active subscriptions" }
            emptyList()
        }
    }

    /**
     * Get subscription by ID
     */
    suspend fun getSubscriptionById(id: Long): Subscription? = withContext(Dispatchers.IO) {
        try {
            logger.info { "Fetching subscription with ID: $id" }
            client.get("$baseUrl/$id").body()
        } catch (e: Exception) {
            logger.error(e) { "Error fetching subscription with ID: $id" }
            null
        }
    }

    /**
     * Search subscriptions by name
     */
    suspend fun searchSubscriptions(name: String): List<Subscription> = withContext(Dispatchers.IO) {
        try {
            logger.info { "Searching subscriptions with name: $name" }
            val response: HttpResponse = client.get("$baseUrl/search") {
                parameter("name", name)
            }
            response.body<List<Subscription>>()
        } catch (e: Exception) {
            logger.error(e) { "Error searching subscriptions with name: $name" }
            emptyList()
        }
    }

    /**
     * Get subscriptions by frequency
     */
    suspend fun getSubscriptionsByFrequency(frequency: SubscriptionFrequency): List<Subscription> = withContext(Dispatchers.IO) {
        try {
            logger.info { "Fetching subscriptions with frequency: $frequency" }
            val response: HttpResponse = client.get("$baseUrl/by-frequency/$frequency")
            response.body<List<Subscription>>()
        } catch (e: Exception) {
            logger.error(e) { "Error fetching subscriptions with frequency: $frequency" }
            emptyList()
        }
    }

    /**
     * Get subscription totals
     */
    suspend fun getSubscriptionTotals(): Map<String, BigDecimal> = withContext(Dispatchers.IO) {
        try {
            logger.info { "Fetching subscription totals" }
            client.get("$baseUrl/totals").body()
        } catch (e: Exception) {
            logger.error(e) { "Error fetching subscription totals" }
            mapOf("monthlyTotal" to BigDecimal.ZERO, "yearlyTotal" to BigDecimal.ZERO)
        }
    }

    /**
     * Create a new subscription
     */
    suspend fun createSubscription(subscription: Subscription): Subscription? = withContext(Dispatchers.IO) {
        try {
            logger.info { "Creating subscription: $subscription" }
            client.post(baseUrl) {
                contentType(ContentType.Application.Json)
                setBody(subscription)
            }.body()
        } catch (e: Exception) {
            logger.error(e) { "Error creating subscription: $subscription" }
            null
        }
    }

    /**
     * Update an existing subscription
     */
    suspend fun updateSubscription(subscription: Subscription): Subscription? = withContext(Dispatchers.IO) {
        try {
            logger.info { "Updating subscription with ID: ${subscription.id}" }
            client.put("$baseUrl/${subscription.id}") {
                contentType(ContentType.Application.Json)
                setBody(subscription)
            }.body()
        } catch (e: Exception) {
            logger.error(e) { "Error updating subscription with ID: ${subscription.id}" }
            null
        }
    }

    /**
     * Toggle a subscription's active status
     */
    suspend fun toggleSubscriptionActive(id: Long): Subscription? = withContext(Dispatchers.IO) {
        try {
            logger.info { "Toggling active status for subscription with ID: $id" }
            client.patch("$baseUrl/$id/toggle-active").body()
        } catch (e: Exception) {
            logger.error(e) { "Error toggling active status for subscription with ID: $id" }
            null
        }
    }

    /**
     * Delete a subscription
     */
    suspend fun deleteSubscription(id: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            logger.info { "Deleting subscription with ID: $id" }
            val response = client.delete("$baseUrl/$id")
            response.status.isSuccess()
        } catch (e: Exception) {
            logger.error(e) { "Error deleting subscription with ID: $id" }
            false
        }
    }

    /**
     * Close the HTTP client
     */
    fun close() {
        client.close()
    }
} 