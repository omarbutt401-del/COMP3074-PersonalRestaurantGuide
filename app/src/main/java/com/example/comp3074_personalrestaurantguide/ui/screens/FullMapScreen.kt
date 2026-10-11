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
import com.example.comp3074_personalrestaurantguide.data.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullMapScreen(restaurantId: Long, onBack: () -> Unit) {
    val context = LocalContext.current
    val r = remember(restaurantId) {
        SampleData.restaurants.firstOrNull { it.id == restaurantId }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(r?.name ?: "Map") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null)
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
                        Icon(Icons.Filled.LocationOn, null)
                        Spacer(Modifier.height(8.dp))
                        Text(r?.address ?: "")
                        Text("Lat ${r?.latitude}, Lng ${r?.longitude}")
                        Spacer(Modifier.height(8.dp))
                        Text("(Full-screen Google Map in final prototype)",
                            style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Button(
                onClick = {
                    r?.let { item ->
                        val uri = Uri.parse(
                            "google.navigation:q=${item.latitude},${item.longitude}")
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                            .setPackage("com.google.android.apps.maps")
                        runCatching { context.startActivity(intent) }
                            .onFailure {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW,
                                        Uri.parse("geo:${item.latitude},${item.longitude}"))
                                )
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) { Text("Get Directions") }
        }
    }
}