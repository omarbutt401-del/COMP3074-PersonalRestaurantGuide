package com.example.comp3074_personalrestaurantguide.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.comp3074_personalrestaurantguide.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullMapScreen(
    restaurantId: Long,
    onBack: () -> Unit,
    viewModel: RestaurantViewModel = viewModel()
) {
    val context = LocalContext.current

    val restaurants by viewModel.restaurants.collectAsState()
    val restaurant = restaurants.find { it.id == restaurantId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(restaurant?.name ?: "Map") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier.weight(1f).fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Filled.LocationOn, contentDescription = null)
                        Spacer(Modifier.height(8.dp))
                        Text(restaurant?.address ?: "No address provided")
                        Text("Lat ${restaurant?.latitude ?: 0.0}, Lng ${restaurant?.longitude ?: 0.0}")
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "(Full-screen Google Map in final prototype)",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Button(
                onClick = {
                    restaurant?.let { rest ->
                        val uri = Uri.parse("google.navigation:q=${rest.latitude},${rest.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                            .setPackage("com.google.android.apps.maps")
                        runCatching { context.startActivity(intent) }
                            .onFailure {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("geo:${rest.latitude},${rest.longitude}")
                                    )
                                )
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                enabled = restaurant != null
            ) {
                Text("Get Directions")
            }
        }
    }
}