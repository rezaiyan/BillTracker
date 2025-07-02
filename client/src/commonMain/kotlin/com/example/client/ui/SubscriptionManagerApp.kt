package com.example.client.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.client.api.SubscriptionApiClient
import com.example.client.model.Subscription
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionManagerApp() {
    val apiClient = remember { SubscriptionApiClient() }
    val scope = rememberCoroutineScope()
    
    var currentScreen by remember { mutableStateOf<Screen>(Screen.SubscriptionList) }
    var subscriptions by remember { mutableStateOf<List<Subscription>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedSubscription by remember { mutableStateOf<Subscription?>(null) }
    var totals by remember { mutableStateOf<Map<String, java.math.BigDecimal>>(emptyMap()) }
    
    // Load data when the app starts
    LaunchedEffect(Unit) {
        scope.launch {
            loadData(apiClient) { subs, totalsMap ->
                subscriptions = subs
                totals = totalsMap
            }
        }
    }
    
    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Subscription Manager") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Navigation tabs
                TabRow(
                    selectedTabIndex = when (currentScreen) {
                        Screen.SubscriptionList -> 0
                        Screen.AddSubscription -> 1
                        Screen.Statistics -> 2
                        Screen.EditSubscription -> 0 // treat edit as list tab
                    }
                ) {
                    Tab(
                        selected = currentScreen == Screen.SubscriptionList,
                        onClick = { currentScreen = Screen.SubscriptionList }
                    ) {
                        Text("Subscriptions", modifier = Modifier.padding(16.dp))
                    }
                    Tab(
                        selected = currentScreen == Screen.AddSubscription,
                        onClick = { currentScreen = Screen.AddSubscription }
                    ) {
                        Text("Add New", modifier = Modifier.padding(16.dp))
                    }
                    Tab(
                        selected = currentScreen == Screen.Statistics,
                        onClick = { currentScreen = Screen.Statistics }
                    ) {
                        Text("Statistics", modifier = Modifier.padding(16.dp))
                    }
                }
                
                // Error message
                errorMessage?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
                
                // Loading indicator
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                // Screen content
                when (currentScreen) {
                    Screen.SubscriptionList -> {
                        SubscriptionListScreen(
                            subscriptions = subscriptions,
                            onEdit = { subscription ->
                                selectedSubscription = subscription
                                currentScreen = Screen.EditSubscription
                            },
                            onDelete = { subscription ->
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    val success = apiClient.deleteSubscription(subscription.id)
                                    if (success) {
                                        loadData(apiClient) { subs, totalsMap ->
                                            subscriptions = subs
                                            totals = totalsMap
                                        }
                                    } else {
                                        errorMessage = "Failed to delete subscription"
                                    }
                                    isLoading = false
                                }
                            },
                            onToggleActive = { subscription ->
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    val updated = apiClient.toggleSubscriptionActive(subscription.id)
                                    if (updated != null) {
                                        loadData(apiClient) { subs, totalsMap ->
                                            subscriptions = subs
                                            totals = totalsMap
                                        }
                                    } else {
                                        errorMessage = "Failed to toggle subscription status"
                                    }
                                    isLoading = false
                                }
                            }
                        )
                    }
                    Screen.AddSubscription -> {
                        SubscriptionFormScreen(
                            subscription = null,
                            onSave = { subscription ->
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    val created = apiClient.createSubscription(subscription)
                                    if (created != null) {
                                        loadData(apiClient) { subs, totalsMap ->
                                            subscriptions = subs
                                            totals = totalsMap
                                        }
                                        currentScreen = Screen.SubscriptionList
                                    } else {
                                        errorMessage = "Failed to create subscription"
                                    }
                                    isLoading = false
                                }
                            },
                            onCancel = {
                                currentScreen = Screen.SubscriptionList
                            }
                        )
                    }
                    Screen.EditSubscription -> {
                        selectedSubscription?.let { subscription ->
                            SubscriptionFormScreen(
                                subscription = subscription,
                                onSave = { updatedSubscription ->
                                    scope.launch {
                                        isLoading = true
                                        errorMessage = null
                                        val updated = apiClient.updateSubscription(updatedSubscription)
                                        if (updated != null) {
                                            loadData(apiClient) { subs, totalsMap ->
                                                subscriptions = subs
                                                totals = totalsMap
                                            }
                                            currentScreen = Screen.SubscriptionList
                                        } else {
                                            errorMessage = "Failed to update subscription"
                                        }
                                        isLoading = false
                                    }
                                },
                                onCancel = {
                                    currentScreen = Screen.SubscriptionList
                                }
                            )
                        }
                    }
                    Screen.Statistics -> {
                        StatisticsScreen(totals = totals)
                    }
                }
            }
        }
    }
}

private suspend fun loadData(
    apiClient: SubscriptionApiClient,
    onDataLoaded: (List<Subscription>, Map<String, java.math.BigDecimal>) -> Unit
) {
    val subs = apiClient.getAllSubscriptions()
    val totals = apiClient.getSubscriptionTotals()
    onDataLoaded(subs, totals)
}

sealed class Screen {
    object SubscriptionList : Screen()
    object AddSubscription : Screen()
    object EditSubscription : Screen()
    object Statistics : Screen()
} 