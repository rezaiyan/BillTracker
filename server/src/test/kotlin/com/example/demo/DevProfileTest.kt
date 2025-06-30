package com.example.demo

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class DevProfileTest {

    @Test
    fun `application starts with dev profile`() {
        // This test will pass if the application context loads successfully with the dev profile
    }
}