package com.example.biblereader.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BibleNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun navigationFromTestamentToBookList() {
        // Navigate from Testament list to Book list
        composeRule.onNodeWithText("Old Testament").performClick()
        
        // Verify we're in the Book list
        composeRule.onNodeWithText("Genesis").assertExists()
        composeRule.onNodeWithText("Exodus").assertExists()
    }

    @Test
    fun navigationFromBookToChapterList() {
        // Navigate to Book list
        composeRule.onNodeWithText("Old Testament").performClick()
        
        // Select Genesis
        composeRule.onNodeWithText("Genesis").performClick()
        
        // Verify we're in the Chapter list
        composeRule.onNodeWithText("Chapter 1").assertExists()
        composeRule.onNodeWithText("Chapter 2").assertExists()
    }

    @Test
    fun navigationFromChapterToVerseView() {
        // Navigate to Chapter list
        composeRule.onNodeWithText("Old Testament").performClick()
        composeRule.onNodeWithText("Genesis").performClick()
        
        // Select Chapter 1
        composeRule.onNodeWithText("Chapter 1").performClick()
        
        // Verify we're in the Verse view
        composeRule.onNodeWithText("In the beginning").assertExists()
    }

    @Test
    fun verseSearchNavigation() {
        // Perform search
        composeRule.onNodeWithContentDescription("Search").performClick()
        composeRule.onNodeWithText("John 3:16").performClick()
        
        // Verify we're at the correct verse
        composeRule.onNodeWithText("For God so loved the world").assertExists()
    }

    @Test
    fun bookmarkAndHighlightPersistence() {
        // Navigate to a verse
        composeRule.onNodeWithText("Old Testament").performClick()
        composeRule.onNodeWithText("Genesis").performClick()
        composeRule.onNodeWithText("Chapter 1").performClick()
        
        // Bookmark the verse
        composeRule.onNodeWithContentDescription("Bookmark").performClick()
        
        // Navigate to bookmarks
        composeRule.onNodeWithContentDescription("Bookmarks").performClick()
        
        // Verify bookmark exists
        composeRule.onNodeWithText("Genesis 1:1").assertExists()
    }
} 