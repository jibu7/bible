package com.example.biblereader.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.biblereader.data.local.preferences.ThemePreferences

@Composable
fun BiblereaderTheme(
    themeManager: ThemeManager,
    content: @Composable () -> Unit
) {
    val isDarkTheme by themeManager.isDarkTheme.collectAsState(initial = false)
    
    BibleReaderTheme(
        darkTheme = isDarkTheme,
        content = content
    )
} 