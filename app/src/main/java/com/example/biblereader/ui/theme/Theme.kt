package com.example.biblereader.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1B5E20),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF002105),
    secondary = Color(0xFF2E7D32),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB8F5B9),
    onSecondaryContainer = Color(0xFF002204),
    tertiary = Color(0xFF1565C0),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1E4FF),
    onTertiaryContainer = Color(0xFF001D36),
    background = Color(0xFFFCFDF6),
    onBackground = Color(0xFF1A1C18),
    surface = Color(0xFFFCFDF6),
    onSurface = Color(0xFF1A1C18),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9CCC65),
    onPrimary = Color(0xFF003909),
    primaryContainer = Color(0xFF005312),
    onPrimaryContainer = Color(0xFFB8F5B9),
    secondary = Color(0xFF9CCC65),
    onSecondary = Color(0xFF003909),
    secondaryContainer = Color(0xFF005312),
    onSecondaryContainer = Color(0xFFB8F5B9),
    tertiary = Color(0xFF9ECAFF),
    onTertiary = Color(0xFF003258),
    tertiaryContainer = Color(0xFF004881),
    onTertiaryContainer = Color(0xFFD1E4FF),
    background = Color(0xFF1A1C18),
    onBackground = Color(0xFFE2E3DD),
    surface = Color(0xFF1A1C18),
    onSurface = Color(0xFFE2E3DD),
)

@Composable
fun BibleReaderTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = !darkTheme
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Simple preview without using the actual ThemeManager
@Preview(showBackground = true)
@Composable
fun BiblereaderThemePreview() {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography
    ) {
        Text("Hello, Bible Reader!")
    }
}