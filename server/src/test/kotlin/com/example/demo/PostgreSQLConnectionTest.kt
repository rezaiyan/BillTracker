package com.example.demo

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace

@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // Use the real database
class PostgreSQLConnectionTest {

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Test
    fun testDatabaseConnection() {
        try {
            // Try to execute a simple query
            val result = jdbcTemplate.queryForObject("SELECT 1", Int::class.java)
            println("[DEBUG_LOG] Successfully connected to PostgreSQL database")
            println("[DEBUG_LOG] Query result: $result")
            assertNotNull(result)
        } catch (e: Exception) {
            println("[DEBUG_LOG] Failed to connect to PostgreSQL database")
            println("[DEBUG_LOG] Error: ${e.message}")
            println("[DEBUG_LOG] Error type: ${e.javaClass.name}")
            // Print the cause if available
            e.cause?.let {
                println("[DEBUG_LOG] Cause: ${it.message}")
                println("[DEBUG_LOG] Cause type: ${it.javaClass.name}")
            }
            throw e // Rethrow to fail the test
        }
    }
}
