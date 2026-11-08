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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp3074_personalrestaurantguide.data.Restaurant
import com.example.comp3074_personalrestaurantguide.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditRestaurantScreen(
    restaurantId: Long?,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: RestaurantViewModel = viewModel()
) {

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var tagsText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(0) }
    var nameError by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (restaurantId != null) "Edit Restaurant" else "Add Restaurant") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name, onValueChange = { name = it; nameError = false },
                label = { Text("Name *") }, isError = nameError,
                supportingText = { if (nameError) Text("Name is required") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address, onValueChange = { address = it },
                label = { Text("Address") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Phone") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description, onValueChange = { description = it },
                label = { Text("Description") }, minLines = 3, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = tagsText, onValueChange = { tagsText = it },
                label = { Text("Tags (comma-separated)") },
                supportingText = { Text("e.g. Italian, vegetarian, date-night") },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Text("Rating", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { i ->
                    IconButton(onClick = { rating = i + 1 }) {
                        Icon(
                            Icons.Filled.Star, contentDescription = "Star ${i + 1}",
                            tint = if (i < rating) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
                Text(if (rating == 0) "Not rated" else "$rating / 5")
            }

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        if (name.isBlank()) { nameError = true; return@Button }
                        val r = Restaurant(
                            id = restaurantId ?: 0L,
                            name = name.trim(),
                            address = address.trim(),
                            phone = phone.trim(),
                            description = description.trim(),
                            tags = tagsText.trim(),
                            rating = if (rating == 0) 1 else rating
                        )
                        if (restaurantId != null) viewModel.updateRestaurant(r)
                        else viewModel.addRestaurant(r)
                        onSaved()
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }
            }
        }
    }
}