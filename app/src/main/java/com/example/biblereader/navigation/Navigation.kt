package com.example.biblereader.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.example.biblereader.ui.animation.BibleAnimations

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object BookList : Screen("books")
    object ChapterList : Screen("chapters/{bookId}") {
        fun createRoute(bookId: String) = "chapters/$bookId"
    }
    object VerseList : Screen("verses/{bookId}/{chapterId}") {
        fun createRoute(bookId: String, chapterId: String) = "verses/$bookId/$chapterId"
    }
    object VerseDetail : Screen("verse/{bookId}/{chapterId}/{verseId}") {
        fun createRoute(bookId: String, chapterId: String, verseId: String) = 
            "verse/$bookId/$chapterId/$verseId"
    }
    object Search : Screen("search")
    object Bookmarks : Screen("bookmarks")
    object Settings : Screen("settings")
}

@ExperimentalAnimationApi
@Composable
fun BibleNavigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.Home.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) {
            // HomeScreen()
        }
        
        composable(
            route = Screen.BookList.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) {
            // BookListScreen()
        }
        
        composable(
            route = Screen.ChapterList.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            // ChapterListScreen(bookId = bookId)
        }
        
        composable(
            route = Screen.VerseList.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable
            // VerseListScreen(bookId = bookId, chapterId = chapterId)
        }
        
        composable(
            route = Screen.VerseDetail.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getString("bookId") ?: return@composable
            val chapterId = backStackEntry.arguments?.getString("chapterId") ?: return@composable
            val verseId = backStackEntry.arguments?.getString("verseId") ?: return@composable
            // VerseDetailScreen(bookId = bookId, chapterId = chapterId, verseId = verseId)
        }
        
        composable(
            route = Screen.Search.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) {
            // SearchScreen()
        }
        
        composable(
            route = Screen.Bookmarks.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) {
            // BookmarksScreen()
        }
        
        composable(
            route = Screen.Settings.route,
            enterTransition = BibleAnimations.enterTransition,
            exitTransition = BibleAnimations.exitTransition,
            popEnterTransition = BibleAnimations.popEnterTransition,
            popExitTransition = BibleAnimations.popExitTransition
        ) {
            // SettingsScreen()
        }
    }
} 