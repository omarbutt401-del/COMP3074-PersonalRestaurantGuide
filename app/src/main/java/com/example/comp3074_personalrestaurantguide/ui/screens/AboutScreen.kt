package com.example.comp3074_personalrestaurantguide.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier.padding(padding).padding(24.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Personal Restaurant Guide",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Text("COMP3074 – Mobile Application Development I")
            Text("Team P18")
            Spacer(Modifier.height(16.dp))
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Team Members", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text("• Omar Butt — ID 101550625")
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Version 1.0 (UI Prototype)",
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}