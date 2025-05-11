package com.example.biblereader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.biblereader.ui.theme.BiblereaderTheme
import com.example.biblereader.ui.theme.ThemeManager
import com.example.biblereader.ui.screens.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var themeManager: ThemeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiblereaderTheme(themeManager = themeManager) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "testaments"
    ) {
        composable("testaments") {
            TestamentListScreen(navController = navController)
        }

        composable("books/{testament}") { backStackEntry ->
            BookListScreen(navController = navController)
        }

        composable(
            route = "chapters/{bookId}",
            arguments = listOf(
                navArgument("bookId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            ChapterListScreen(navController = navController)
        }

        composable(
            route = "verses/{chapterId}",
            arguments = listOf(
                navArgument("chapterId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            VerseViewScreen(navController = navController)
        }

        composable("search") {
            SearchScreen(navController = navController)
        }

        composable("user_data") {
            BookmarksHighlightsScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }

        composable("export_import") {
            ExportImportScreen(navController = navController)
        }
    }
}