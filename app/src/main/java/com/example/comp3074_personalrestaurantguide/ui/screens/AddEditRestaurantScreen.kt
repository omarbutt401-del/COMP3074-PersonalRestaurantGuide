package com.example.comp3074_personalrestaurantguide.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.comp3074_personalrestaurantguide.data.SampleData
import com.example.comp3074_personalrestaurantguide.model.Restaurant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRestaurantScreen(
    restaurantId: Long?,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val existing = restaurantId?.let { id ->
        SampleData.restaurants.firstOrNull { it.id == id }
    }
    val isEdit = existing != null

    var name by remember { mutableStateOf(existing?.name ?: "") }
    var address by remember { mutableStateOf(existing?.address ?: "") }
    var phone by remember { mutableStateOf(existing?.phone ?: "") }
    var description by remember { mutableStateOf(existing?.description ?: "") }
    var tagsText by remember {
        mutableStateOf(existing?.tags?.joinToString(", ") ?: "")
    }
    var rating by remember { mutableStateOf(existing?.rating ?: 0) }
    var nameError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit Restaurant" else "Add Restaurant") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = false },
                label = { Text("Name *") },
                isError = nameError,
                supportingText = { if (nameError) Text("Name is required") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address, onValueChange = { address = it },
                label = { Text("Address") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Phone") }, singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Description") }, minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tagsText, onValueChange = { tagsText = it },
                label = { Text("Tags (comma-separated)") },
                supportingText = { Text("e.g. Italian, vegetarian, date-night") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Text("Rating", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { i ->
                    IconButton(onClick = { rating = i + 1 }) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = "Star ${i + 1}",
                            tint = if (i < rating) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(if (rating == 0) "Not rated" else "$rating / 5")
            }

            Spacer(Modifier.height(8.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (name.isBlank()) { nameError = true; return@Button }
                        val tags = tagsText.split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                        val r = Restaurant(
                            id = existing?.id ?: 0L,
                            name = name.trim(),
                            address = address.trim(),
                            phone = phone.trim(),
                            description = description.trim(),
                            tags = tags,
                            rating = if (rating == 0) 1 else rating,
                            latitude = existing?.latitude ?: 0.0,
                            longitude = existing?.longitude ?: 0.0
                        )
                        if (isEdit) SampleData.updateRestaurant(r)
                        else SampleData.addRestaurant(r)
                        onSaved()
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }
            }
        }
    }
}