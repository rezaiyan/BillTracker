package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var subscriptionRepository: SubscriptionRepository

    @Test
    fun `should save and find subscription`() {
        // Create a subscription
        val subscription = Subscription(
            name = "Netflix",
            amount = BigDecimal("9.99"),
            frequency = SubscriptionFrequency.MONTHLY
        )

        // Save the subscription
        val savedSubscription = subscriptionRepository.save(subscription)

        // Clear persistence context to ensure we're getting from the database
        entityManager.flush()
        entityManager.clear()

        // Find the subscription by ID
        val foundSubscription = subscriptionRepository.findById(savedSubscription.id).orElse(null)

        // Verify the subscription was found and has the correct properties
        assertNotNull(foundSubscription)
        assertEquals("Netflix", foundSubscription.name)
        assertEquals(BigDecimal("9.99"), foundSubscription.amount)
        assertEquals(SubscriptionFrequency.MONTHLY, foundSubscription.frequency)
        assertNull(foundSubscription.description)
        assertTrue(foundSubscription.active)
    }

    @Test
    fun `should find subscriptions by frequency`() {
        // Create subscriptions with different frequencies
        val monthlySubscription = Subscription(
            name = "Spotify",
            amount = BigDecimal("9.99"),
            frequency = SubscriptionFrequency.MONTHLY
        )

        val yearlySubscription = Subscription(
            name = "Amazon Prime",
            amount = BigDecimal("119.00"),
            frequency = SubscriptionFrequency.YEARLY
        )

        // Save the subscriptions
        subscriptionRepository.save(monthlySubscription)
        subscriptionRepository.save(yearlySubscription)

        // Clear persistence context
        entityManager.flush()
        entityManager.clear()

        // Find subscriptions by frequency
        val monthlySubscriptions = subscriptionRepository.findByFrequency(SubscriptionFrequency.MONTHLY)
        val yearlySubscriptions = subscriptionRepository.findByFrequency(SubscriptionFrequency.YEARLY)

        // Verify results
        assertEquals(1, monthlySubscriptions.size)
        assertEquals("Spotify", monthlySubscriptions[0].name)

        assertEquals(1, yearlySubscriptions.size)
        assertEquals("Amazon Prime", yearlySubscriptions[0].name)
    }

    @Test
    fun `should calculate total monthly and yearly amounts`() {
        // Create subscriptions with different frequencies
        val netflixSubscription = Subscription(
            name = "Netflix",
            amount = BigDecimal("9.99"),
            frequency = SubscriptionFrequency.MONTHLY
        )

        val spotifySubscription = Subscription(
            name = "Spotify",
            amount = BigDecimal("12.99"),
            frequency = SubscriptionFrequency.MONTHLY
        )

        val amazonPrimeSubscription = Subscription(
            name = "Amazon Prime",
            amount = BigDecimal("119.00"),
            frequency = SubscriptionFrequency.YEARLY
        )

        // Save the subscriptions
        subscriptionRepository.saveAll(listOf(netflixSubscription, spotifySubscription, amazonPrimeSubscription))

        // Clear persistence context
        entityManager.flush()
        entityManager.clear()

        // Calculate totals
        val monthlyTotal = subscriptionRepository.calculateTotalMonthlyAmount()
        val yearlyTotal = subscriptionRepository.calculateTotalYearlyAmount()

        // Verify results (monthly: 9.99 + 12.99 + (119.00/12) = 22.98 + 9.92 = 32.90)
        // (yearly: (9.99*12) + (12.99*12) + 119.00 = 119.88 + 155.88 + 119.00 = 394.76)
        assertNotNull(monthlyTotal)
        assertNotNull(yearlyTotal)

        // Using compareTo because of potential floating-point precision issues
        assertTrue(BigDecimal("32.90").compareTo(monthlyTotal) == 0 || 
                  BigDecimal("32.90").subtract(monthlyTotal).abs() < BigDecimal("0.01"))

        assertTrue(BigDecimal("394.76").compareTo(yearlyTotal) == 0 || 
                  BigDecimal("394.76").subtract(yearlyTotal).abs() < BigDecimal("0.01"))
    }
}
