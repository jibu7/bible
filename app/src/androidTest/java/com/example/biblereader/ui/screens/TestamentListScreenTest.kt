package com.example.biblereader.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.ui.theme.BiblereaderTheme
import com.example.biblereader.ui.theme.ThemeManager
import com.example.biblereader.ui.viewmodels.TestamentDisplay
import com.example.biblereader.ui.viewmodels.TestamentListViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TestamentListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController
    private lateinit var viewModel: TestamentListViewModel
    private lateinit var themeManager: ThemeManager

    @Before
    fun setup() {
        navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        
        // Mock the ViewModel
        viewModel = mockk(relaxed = true)
        
        // Mock the ThemeManager
        themeManager = mockk(relaxed = true)
        
        // Set up default values for the ViewModel
        every { viewModel.testaments } returns MutableStateFlow(
            listOf(
                TestamentDisplay("Isezerano rya Kera"),
                TestamentDisplay("Isezerano Rishya")
            )
        )
    }

    @Test
    fun testamentListScreen_displaysTitle() {
        // Arrange & Act
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                TestamentListScreen(navController = navController, viewModel = viewModel)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Bible Reader").assertExists()
    }

    @Test
    fun testamentListScreen_displaysTestaments() {
        // Arrange & Act
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                TestamentListScreen(navController = navController, viewModel = viewModel)
            }
        }

        // Assert
        composeTestRule.onNodeWithText("Isezerano rya Kera").assertExists()
        composeTestRule.onNodeWithText("Isezerano Rishya").assertExists()
    }

    @Test
    fun testamentListScreen_displaysSettingsButton() {
        // Arrange & Act
        composeTestRule.setContent {
            BiblereaderTheme(themeManager = themeManager) {
                TestamentListScreen(navController = navController, viewModel = viewModel)
            }
        }

        // Assert
        composeTestRule.onNodeWithContentDescription("Settings").assertExists()
    }
} 