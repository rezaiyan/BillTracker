package com.example.frontend

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.frontend.ui.SubscriptionManagerApp
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() = application {
    logger.info { "Starting Subscription Manager Frontend Application" }
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Subscription Manager",
        state = rememberWindowState()
    ) {
        SubscriptionManagerApp()
    }
} 