package com.example.frontend

import com.example.frontend.api.SubscriptionApiClient
import com.example.frontend.model.Subscription
import com.example.frontend.model.SubscriptionFrequency
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import java.math.BigDecimal
import java.time.Instant

class FrontendApplicationTest {
    
    private val apiClient = SubscriptionApiClient()
    
    @Test
    fun testApiClientConnection() = runBlocking {
        // Test that we can connect to the backend
        val subscriptions = apiClient.getAllSubscriptions()
        assertNotNull(subscriptions)
    }
    
    @Test
    fun testCreateAndDeleteSubscription() = runBlocking {
        // Create a test subscription
        val testSubscription = Subscription(
            name = "Test Subscription",
            description = "Test subscription for frontend testing",
            amount = BigDecimal("9.99"),
            frequency = SubscriptionFrequency.MONTHLY,
            active = true
        )
        
        // Create the subscription
        val created = apiClient.createSubscription(testSubscription)
        assertNotNull(created)
        assertTrue(created.id > 0)
        assertEquals(testSubscription.name, created.name)
        assertEquals(testSubscription.amount, created.amount)
        assertEquals(testSubscription.frequency, created.frequency)
        
        // Delete the subscription
        val deleted = apiClient.deleteSubscription(created.id)
        assertTrue(deleted)
    }
    
    @Test
    fun testUpdateSubscription() = runBlocking {
        // Create a test subscription
        val testSubscription = Subscription(
            name = "Update Test",
            description = "Test subscription for update testing",
            amount = BigDecimal("19.99"),
            frequency = SubscriptionFrequency.YEARLY,
            active = true
        )
        
        // Create the subscription
        val created = apiClient.createSubscription(testSubscription)
        assertNotNull(created)
        
        // Update the subscription
        val updatedSubscription = created.copy(
            name = "Updated Test Subscription",
            amount = BigDecimal("29.99")
        )
        
        val updated = apiClient.updateSubscription(updatedSubscription)
        assertNotNull(updated)
        assertEquals("Updated Test Subscription", updated.name)
        assertEquals(BigDecimal("29.99"), updated.amount)
        
        // Clean up
        apiClient.deleteSubscription(updated.id)
    }
    
    @Test
    fun testToggleSubscriptionActive() = runBlocking {
        // Create a test subscription
        val testSubscription = Subscription(
            name = "Toggle Test",
            description = "Test subscription for toggle testing",
            amount = BigDecimal("5.99"),
            frequency = SubscriptionFrequency.MONTHLY,
            active = true
        )
        
        // Create the subscription
        val created = apiClient.createSubscription(testSubscription)
        assertNotNull(created)
        assertTrue(created.active)
        
        // Toggle to inactive
        val toggled = apiClient.toggleSubscriptionActive(created.id)
        assertNotNull(toggled)
        assertTrue(!toggled.active)
        
        // Toggle back to active
        val toggledBack = apiClient.toggleSubscriptionActive(created.id)
        assertNotNull(toggledBack)
        assertTrue(toggledBack.active)
        
        // Clean up
        apiClient.deleteSubscription(created.id)
    }
    
    @Test
    fun testGetSubscriptionTotals() = runBlocking {
        val totals = apiClient.getSubscriptionTotals()
        assertNotNull(totals)
        assertTrue(totals.containsKey("monthlyTotal"))
        assertTrue(totals.containsKey("yearlyTotal"))
        
        val monthlyTotal = totals["monthlyTotal"]
        val yearlyTotal = totals["yearlyTotal"]
        
        assertNotNull(monthlyTotal)
        assertNotNull(yearlyTotal)
        assertTrue(monthlyTotal >= BigDecimal.ZERO)
        assertTrue(yearlyTotal >= BigDecimal.ZERO)
    }
    
    @Test
    fun testSearchSubscriptions() = runBlocking {
        // Create a test subscription with a unique name
        val uniqueName = "SearchTest_${Instant.now().toEpochMilli()}"
        val testSubscription = Subscription(
            name = uniqueName,
            description = "Test subscription for search testing",
            amount = BigDecimal("7.99"),
            frequency = SubscriptionFrequency.MONTHLY,
            active = true
        )
        
        // Create the subscription
        val created = apiClient.createSubscription(testSubscription)
        assertNotNull(created)
        
        // Search for the subscription
        val searchResults = apiClient.searchSubscriptions("SearchTest")
        assertTrue(searchResults.isNotEmpty())
        assertTrue(searchResults.any { it.name.contains("SearchTest") })
        
        // Clean up
        apiClient.deleteSubscription(created.id)
    }
    
    @Test
    fun testGetSubscriptionsByFrequency() = runBlocking {
        // Test getting monthly subscriptions
        val monthlySubscriptions = apiClient.getSubscriptionsByFrequency(SubscriptionFrequency.MONTHLY)
        assertNotNull(monthlySubscriptions)
        
        // Test getting yearly subscriptions
        val yearlySubscriptions = apiClient.getSubscriptionsByFrequency(SubscriptionFrequency.YEARLY)
        assertNotNull(yearlySubscriptions)
        
        // Verify all returned subscriptions have the correct frequency
        monthlySubscriptions.forEach { subscription ->
            assertEquals(SubscriptionFrequency.MONTHLY, subscription.frequency)
        }
        
        yearlySubscriptions.forEach { subscription ->
            assertEquals(SubscriptionFrequency.YEARLY, subscription.frequency)
        }
    }
} 