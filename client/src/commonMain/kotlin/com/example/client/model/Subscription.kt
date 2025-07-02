package com.example.client.model

import com.example.client.util.BigDecimalSerializer
import com.example.client.util.InstantSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant

/**
 * Enum representing the frequency of a subscription (monthly or yearly)
 */
@Serializable
enum class SubscriptionFrequency {
    MONTHLY,
    YEARLY
}

/**
 * Data class representing a subscription
 */
@Serializable
data class Subscription(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val frequency: SubscriptionFrequency,
    @Serializable(with = InstantSerializer::class)
    val startDate: Instant = Instant.now(),
    @Serializable(with = InstantSerializer::class)
    val nextBillingDate: Instant? = null,
    val active: Boolean = true,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant = Instant.now()
) {
    /**
     * Calculate monthly amount for yearly subscriptions
     */
    fun getMonthlyAmount(): BigDecimal {
        return when (frequency) {
            SubscriptionFrequency.MONTHLY -> amount
            SubscriptionFrequency.YEARLY -> amount.divide(BigDecimal(12), 2, BigDecimal.ROUND_HALF_UP)
        }
    }

    /**
     * Calculate yearly amount for monthly subscriptions
     */
    fun getYearlyAmount(): BigDecimal {
        return when (frequency) {
            SubscriptionFrequency.MONTHLY -> amount.multiply(BigDecimal(12))
            SubscriptionFrequency.YEARLY -> amount
        }
    }
} 