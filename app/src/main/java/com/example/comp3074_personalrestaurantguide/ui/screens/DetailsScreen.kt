package com.example.comp3074_personalrestaurantguide.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.comp3074_personalrestaurantguide.data.SampleData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    restaurantId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onViewMap: (Long) -> Unit
) {
    val context = LocalContext.current
    val r = remember(restaurantId) {
        SampleData.restaurants.firstOrNull { it.id == restaurantId }
    }
    if (r == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Restaurant not found.")
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val body = """
                            ${r.name}
                            ${r.address}
                            ${r.phone}
                            ${r.description}
                            Rating: ${r.rating}/5
                            Tags: ${r.tags.joinToString { "#$it" }}
                        """.trimIndent()
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Restaurant: ${r.name}")
                            putExtra(Intent.EXTRA_TEXT, body)
                        }
                        context.startActivity(Intent.createChooser(intent, "Share via"))
                    }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(r.name, style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(5) { i ->
                    Icon(Icons.Filled.Star, null,
                        tint = if (i < r.rating) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(8.dp))
                Text("${r.rating} / 5")
            }
            Divider()
            InfoRow("Address", r.address)
            InfoRow("Phone", r.phone)
            InfoRow("Description", r.description)
            InfoRow("Tags", r.tags.joinToString("  ") { "#$it" })

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.LocationOn, null, tint = Color.Red)
                    Spacer(Modifier.height(6.dp))
                    Text("Map preview", style = MaterialTheme.typography.labelLarge)
                    Text("Lat ${r.latitude}, Lng ${r.longitude}",
                        style = MaterialTheme.typography.labelSmall)
                }
            }

            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onViewMap(r.id) },
                    modifier = Modifier.weight(1f)) {
                    Text("View Full Map")
                }
                OutlinedButton(onClick = {
                    val uri = Uri.parse("google.navigation:q=${r.latitude},${r.longitude}")
                    val i = Intent(Intent.ACTION_VIEW, uri)
                    i.setPackage("com.google.android.apps.maps")
                    runCatching { context.startActivity(i) }
                        .onFailure {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW,
                                    Uri.parse("geo:${r.latitude},${r.longitude}"))
                            )
                        }
                }, modifier = Modifier.weight(1f)) {
                    Text("Directions")
                }
            }
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onEdit(r.id) },
                    modifier = Modifier.weight(1f)) { Text("Edit") }
                OutlinedButton(onClick = {
                    SampleData.deleteRestaurant(r.id); onBack()
                }, modifier = Modifier.weight(1f)) { Text("Delete") }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold)
        Text(if (value.isBlank()) "—" else value,
            style = MaterialTheme.typography.bodyLarge)
    }
}