package com.example.demo

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals

class SubscriptionTest {

    @Test
    fun `test monthly subscription calculations`() {
        val subscription = Subscription(
            name = "Netflix",
            amount = BigDecimal("9.99"),
            frequency = SubscriptionFrequency.MONTHLY
        )
        
        assertEquals(BigDecimal("9.99"), subscription.getMonthlyAmount())
        assertEquals(BigDecimal("119.88"), subscription.getYearlyAmount())
    }
    
    @Test
    fun `test yearly subscription calculations`() {
        val subscription = Subscription(
            name = "Amazon Prime",
            amount = BigDecimal("119.00"),
            frequency = SubscriptionFrequency.YEARLY
        )
        
        assertEquals(BigDecimal("9.92"), subscription.getMonthlyAmount())
        assertEquals(BigDecimal("119.00"), subscription.getYearlyAmount())
    }
}