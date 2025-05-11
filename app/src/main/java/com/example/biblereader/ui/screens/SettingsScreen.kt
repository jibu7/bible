package com.example.biblereader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.data.local.db.entities.LanguageEntity
import com.example.biblereader.ui.theme.ThemeManager
import com.example.biblereader.ui.viewmodels.SettingsViewModel
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val availableLanguages by viewModel.availableLanguages.collectAsState()
    val selectedLanguageId by viewModel.selectedLanguageId.collectAsState()
    val isDarkModeForced by viewModel.isDarkModeForced.collectAsState()
    val isDarkModeEnabled by viewModel.isDarkModeEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Theme Settings Section
            item {
                Text(
                    "Theme Settings",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Use system theme")
                    Switch(
                        checked = !isDarkModeForced,
                        onCheckedChange = { checked ->
                            viewModel.setDarkModeForced(!checked)
                        }
                    )
                }
            }

            if (isDarkModeForced) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Dark theme")
                        Switch(
                            checked = isDarkModeEnabled,
                            onCheckedChange = { checked ->
                                viewModel.setDarkModeEnabled(checked)
                            }
                        )
                    }
                }
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp)) }

            // Language Settings Section
            item {
                Text(
                    "Language Settings",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            if (availableLanguages.isEmpty()) {
                item { Text("Loading languages...") }
            } else {
                items(availableLanguages) { language ->
                    LanguageItem(
                        language = language,
                        isSelected = language.languageId == selectedLanguageId,
                        onSelect = { viewModel.onLanguageSelected(language.languageId) }
                    )
                }
            }

            item { HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp)) }

            // Other Settings
            item {
                ListItem(
                    headlineContent = { Text("View Bookmarks & Highlights") },
                    modifier = Modifier.clickable { navController.navigate("user_data") }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Export/Import User Data") },
                    modifier = Modifier.clickable { navController.navigate("export_import") }
                )
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: LanguageEntity,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    ListItem(
        headlineContent = { Text(language.languageName) },
        leadingContent = {
            RadioButton(
                selected = isSelected,
                onClick = onSelect
            )
        },
        modifier = Modifier.clickable(onClick = onSelect)
    )
}
