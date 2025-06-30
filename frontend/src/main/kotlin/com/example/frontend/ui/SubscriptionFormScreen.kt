package com.example.frontend.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.frontend.model.Subscription
import com.example.frontend.model.SubscriptionFrequency
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionFormScreen(
    subscription: Subscription?,
    onSave: (Subscription) -> Unit,
    onCancel: () -> Unit
) {
    val isEditing = subscription != null
    
    var name by remember { mutableStateOf(subscription?.name ?: "") }
    var description by remember { mutableStateOf(subscription?.description ?: "") }
    var amountText by remember { mutableStateOf(subscription?.amount?.toString() ?: "") }
    var frequency by remember { mutableStateOf(subscription?.frequency ?: SubscriptionFrequency.MONTHLY) }
    var active by remember { mutableStateOf(subscription?.active ?: true) }
    
    var nameError by remember { mutableStateOf<String?>(null) }
    var amountError by remember { mutableStateOf<String?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = if (isEditing) "Edit Subscription" else "Add New Subscription",
            style = MaterialTheme.typography.headlineMedium
        )
        
        // Name field
        OutlinedTextField(
            value = name,
            onValueChange = { 
                name = it
                nameError = null
            },
            label = { Text("Subscription Name *") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError != null,
            supportingText = nameError?.let { { Text(it) } },
            leadingIcon = {
                Icon(Icons.Default.Label, contentDescription = null)
            }
        )
        
        // Description field
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            leadingIcon = {
                Icon(Icons.Default.Description, contentDescription = null)
            }
        )
        
        // Amount field
        OutlinedTextField(
            value = amountText,
            onValueChange = { 
                amountText = it
                amountError = null
            },
            label = { Text("Amount *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = amountError != null,
            supportingText = amountError?.let { { Text(it) } },
            leadingIcon = {
                Icon(Icons.Default.AttachMoney, contentDescription = null)
            }
        )
        
        // Frequency selection
        Text(
            text = "Billing Frequency *",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SubscriptionFrequency.values().forEach { freq ->
                FilterChip(
                    selected = frequency == freq,
                    onClick = { frequency = freq },
                    label = { Text(freq.name.lowercase().capitalize()) },
                    leadingIcon = {
                        Icon(
                            imageVector = when (freq) {
                                SubscriptionFrequency.MONTHLY -> Icons.Default.Schedule
                                SubscriptionFrequency.YEARLY -> Icons.Default.DateRange
                            },
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Active status
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Switch(
                checked = active,
                onCheckedChange = { active = it }
            )
            Text(
                text = "Active",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Preview card
        val amount = amountText.toBigDecimalOrNull()
        if (name.isNotBlank() && amount != null) {
            val previewSubscription = Subscription(
                name = name,
                description = description.takeIf { it.isNotBlank() },
                amount = amount,
                frequency = frequency,
                active = active
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Monthly cost: $${previewSubscription.getMonthlyAmount()}")
                    Text("Yearly cost: $${previewSubscription.getYearlyAmount()}")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Cancel, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cancel")
            }
            
            Button(
                onClick = {
                    // Validate form
                    var isValid = true
                    
                    if (name.isBlank()) {
                        nameError = "Name is required"
                        isValid = false
                    }
                    
                    if (amountText.isBlank()) {
                        amountError = "Amount is required"
                        isValid = false
                    } else {
                        try {
                            val amount = BigDecimal(amountText)
                            if (amount <= BigDecimal.ZERO) {
                                amountError = "Amount must be greater than 0"
                                isValid = false
                            }
                        } catch (e: NumberFormatException) {
                            amountError = "Invalid amount format"
                            isValid = false
                        }
                    }
                    
                    if (isValid) {
                        val amount = BigDecimal(amountText)
                        val newSubscription = Subscription(
                            id = subscription?.id ?: 0,
                            name = name.trim(),
                            description = description.trim().takeIf { it.isNotBlank() },
                            amount = amount,
                            frequency = frequency,
                            active = active,
                            startDate = subscription?.startDate ?: Instant.now(),
                            nextBillingDate = subscription?.nextBillingDate,
                            createdAt = subscription?.createdAt ?: Instant.now()
                        )
                        onSave(newSubscription)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    if (isEditing) Icons.Default.Save else Icons.Default.Add,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditing) "Save Changes" else "Add Subscription")
            }
        }
    }
}

private fun String.capitalize(): String {
    return if (isNotEmpty()) {
        this[0].uppercase() + substring(1)
    } else {
        this
    }
} 