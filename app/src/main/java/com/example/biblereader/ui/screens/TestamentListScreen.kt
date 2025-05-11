package com.example.biblereader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.ui.viewmodels.TestamentDisplay
import com.example.biblereader.ui.viewmodels.TestamentListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestamentListScreen(
    navController: NavController,
    viewModel: TestamentListViewModel = hiltViewModel() // Hilt provides the ViewModel
) {
    val testaments by viewModel.testaments.collectAsState()

    Scaffold( // Provides basic app structure (AppBar, etc.)
        topBar = {
            TopAppBar(title = { Text("Select Testament") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp) // Spacing between items
            ) {
                items(testaments) { testament ->
                    TestamentItem(
                        testament = testament,
                        onClick = {
                            // Navigate to the appropriate screen based on testament
                            val route = when (testament.name) {
                                "Isezerano rya Kera" -> "books/Old"
                                "Isezerano Rishya" -> "books/New"
                                else -> null
                            }
                            route?.let { navController.navigate(it) }
                        }
                    )
                }
            }
            
            // Settings button at the bottom
            Button(
                onClick = { navController.navigate("settings") },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings")
            }
        }
    }
}

@Composable
fun TestamentItem(
    testament: TestamentDisplay,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = onClick
    ) {
        Text(
            text = testament.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}
