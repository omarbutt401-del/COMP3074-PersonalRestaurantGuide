package com.example.comp3074_personalrestaurantguide.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
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
fun RestaurantListScreen(
    onAdd: () -> Unit,
    onOpen: (Long) -> Unit,
    onAbout: () -> Unit,
    viewModel: RestaurantViewModel = viewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val restaurants by viewModel.restaurants.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Restaurants") },
                actions = {
                    IconButton(onClick = onAbout) {
                        Icon(Icons.Filled.Info, contentDescription = "About")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Filled.Add, contentDescription = "Add restaurant")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Search by name or tag") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (restaurants.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No restaurants match your search.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(restaurants, key = { it.id }) { r ->
                        RestaurantCard(r, onClick = { onOpen(r.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun RestaurantCard(r: Restaurant, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(r.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(r.address, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { i ->
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (i < r.rating) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(r.tags.split(",").joinToString("  ") { "#${it.trim()}" },
                    style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}