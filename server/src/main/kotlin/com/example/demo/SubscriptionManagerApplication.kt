package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.boot.CommandLineRunner
import java.math.BigDecimal
import java.time.Instant

@SpringBootApplication
class SubscriptionManagerApplication {
    @Bean
    fun dataLoader(repository: SubscriptionRepository) = CommandLineRunner {
        if (repository.count() == 0L) {
            repository.saveAll(
                listOf(
                    Subscription(
                        name = "Netflix",
                        description = "Streaming Service",
                        amount = BigDecimal("15.99"),
                        frequency = SubscriptionFrequency.MONTHLY,
                        startDate = Instant.now().minusSeconds(60*60*24*30),
                        active = true
                    ),
                    Subscription(
                        name = "Spotify",
                        description = "Music Streaming",
                        amount = BigDecimal("9.99"),
                        frequency = SubscriptionFrequency.MONTHLY,
                        startDate = Instant.now().minusSeconds(60*60*24*60),
                        active = true
                    ),
                    Subscription(
                        name = "Amazon Prime",
                        description = "Shopping + Video",
                        amount = BigDecimal("119.00"),
                        frequency = SubscriptionFrequency.YEARLY,
                        startDate = Instant.now().minusSeconds(60*60*24*365),
                        active = true
                    ),
                    Subscription(
                        name = "Adobe Creative Cloud",
                        description = "Design Tools",
                        amount = BigDecimal("52.99"),
                        frequency = SubscriptionFrequency.MONTHLY,
                        startDate = Instant.now().minusSeconds(60*60*24*90),
                        active = false
                    )
                )
            )
        }
    }
}

fun main(args: Array<String>) {
    runApplication<SubscriptionManagerApplication>(*args)
}