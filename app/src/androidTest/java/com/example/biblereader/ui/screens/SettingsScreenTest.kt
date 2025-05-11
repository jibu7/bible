package com.example.biblereader.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.TestActivity
import com.example.biblereader.data.local.db.entities.LanguageEntity
import com.example.biblereader.ui.theme.BiblereaderTheme
import com.example.biblereader.ui.theme.ThemeManager
import com.example.biblereader.ui.viewmodels.SettingsViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeManager: ThemeManager

    @Before
    fun setup() {
        hiltRule.inject()
        
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        
        // Mock the ViewModel
        viewModel = mockk(relaxed = true)
        
        // Mock the ThemeManager
        themeManager = mockk(relaxed = true)
        
        // Set up default values for the ViewModel
        every { viewModel.availableLanguages } returns MutableStateFlow(
            listOf(
                LanguageEntity(languageId = 1, languageName = "English", languageCode = "en"),
                LanguageEntity(languageId = 2, languageName = "Kinyarwanda", languageCode = "rw")
            )
        )
        every { viewModel.selectedLanguageId } returns MutableStateFlow(1)
        every { viewModel.isDarkModeForced } returns MutableStateFlow(false)
        every { viewModel.isDarkModeEnabled } returns MutableStateFlow(false)
    }

    @Test
    fun settingsScreen_displaysTitle() {
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Settings").assertExists()
    }

    @Test
    fun settingsScreen_displaysThemeSettings() {
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Theme Settings").assertExists()
        composeTestRule.onNodeWithText("Use system theme").assertExists()
    }

    @Test
    fun settingsScreen_displaysLanguageSettings() {
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Language Settings").assertExists()
        composeTestRule.onNodeWithText("English").assertExists()
        composeTestRule.onNodeWithText("Kinyarwanda").assertExists()
    }

    @Test
    fun settingsScreen_displaysOtherSettings() {
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("View Bookmarks & Highlights").assertExists()
        composeTestRule.onNodeWithText("Export/Import User Data").assertExists()
    }
} 