package com.example.biblereader.ui.theme

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.biblereader.data.local.preferences.ThemePreferences
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ThemeManager @Inject constructor(
    private val context: Context,
    private val themePreferences: ThemePreferences
) {
    open val isDarkTheme: kotlinx.coroutines.flow.Flow<Boolean> = combine(
        themePreferences.isDarkModeForced,
        themePreferences.isDarkModeEnabled
    ) { isForced, isEnabled ->
        when {
            isForced -> isEnabled
            else -> isSystemDarkMode() || isNightTime()
        }
    }

    private fun isSystemDarkMode(): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    private fun isNightTime(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        return hour in 18..23 || hour in 0..5
    }

    suspend fun setDarkModeForced(isForced: Boolean) {
        themePreferences.setDarkModeForced(isForced)
    }

    suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        themePreferences.setDarkModeEnabled(isEnabled)
    }
}

@Composable
fun rememberThemeManager(themeManager: ThemeManager): Boolean {
    val isDarkTheme by themeManager.isDarkTheme.collectAsState(initial = false)
    return isDarkTheme
} 