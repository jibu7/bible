package com.example.biblereader.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.ui.viewmodels.ExportImportViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportImportScreen(
    navController: NavController,
    viewModel: ExportImportViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val exportStatus by viewModel.exportStatus.collectAsState()
    val importStatus by viewModel.importStatus.collectAsState()

    // Permission launcher for storage access
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.exportUserData()
        }
    }

    // File picker launcher for import
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.importUserData(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Export/Import User Data") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Export Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Export Bookmarks and Highlights",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Button(
                        onClick = {
                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    viewModel.exportUserData()
                                }
                                else -> {
                                    permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                }
                            }
                        }
                    ) {
                        Text("Export Data")
                    }

                    if (exportStatus.isNotEmpty()) {
                        Text(
                            text = exportStatus,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            // Import Section
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Import Bookmarks and Highlights",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Button(
                        onClick = {
                            filePickerLauncher.launch("application/json")
                        }
                    ) {
                        Text("Select File to Import")
                    }

                    if (importStatus.isNotEmpty()) {
                        Text(
                            text = importStatus,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
} 