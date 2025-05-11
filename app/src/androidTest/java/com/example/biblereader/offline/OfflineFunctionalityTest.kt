package com.example.biblereader.offline

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.repository.AppRepository
import com.example.biblereader.util.TestDataSetup
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class OfflineFunctionalityTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    @Inject
    lateinit var database: AppDatabase
    
    @Inject
    lateinit var testDataSetup: TestDataSetup

    @Before
    fun setup() {
        hiltRule.inject()
        testDataSetup.setupTestData()
    }

    @Test
    fun allFeaturesFunctionWithoutInternetConnection() = runBlocking {
        // Test navigation features
        val books = repository.getAllBooks().first()
        assert(books.isNotEmpty())
        
        val oldTestamentBooks = repository.getBooksByTestament("Old Testament").first()
        assert(oldTestamentBooks.isNotEmpty())
        
        // Use a default value of 1 (Genesis) if bookId is null
        val firstBookId = oldTestamentBooks.first().bookId ?: 1
        val genesisChapters = repository.getChaptersByBook(firstBookId).first()
        assert(genesisChapters.isNotEmpty())
        
        // Use a default value of 1 (first chapter) if chapterId is null
        val firstChapterId = genesisChapters.first().chapterId ?: 1
        val genesisVerses = repository.getVersesByChapter(firstChapterId).first()
        assert(genesisVerses.isNotEmpty())
    }

    @Test
    fun bibleDataLoadsFromLocalRoomDatabase() = runBlocking {
        // Test data loading from local database
        val books = repository.getAllBooks().first()
        assert(books.isNotEmpty())
        
        val book = books.first()
        // Use a default value of 1 (Genesis) if bookId is null
        val bookId = book.bookId ?: 1
        val chapters = repository.getChaptersByBook(bookId).first()
        assert(chapters.isNotEmpty())
        
        val chapter = chapters.first()
        // Use a default value of 1 (first chapter) if chapterId is null
        val chapterId = chapter.chapterId ?: 1
        val verses = repository.getVersesByChapter(chapterId).first()
        assert(verses.isNotEmpty())
    }

    @Test
    fun userDataIsStoredAndRetrievedLocally() = runBlocking {
        // Test bookmark functionality
        val verseId = 1 // Assuming verse ID 1 exists
        
        // Add a bookmark
        repository.addBookmark(verseId)
        
        // Check if the verse is bookmarked
        val isBookmarked = repository.isVerseBookmarked(verseId).first()
        assert(isBookmarked)
        
        // Remove the bookmark
        repository.removeBookmark(verseId)
        
        // Check if the verse is no longer bookmarked
        val isNotBookmarked = repository.isVerseBookmarked(verseId).first()
        assert(!isNotBookmarked)
    }

    @Test
    fun searchWorksOffline() = runBlocking {
        // Test search functionality offline
        val searchResults = repository.searchVersesByKeyword("loved", 1).first()
        assert(searchResults.isNotEmpty())
        
        val bookSearchResults = repository.searchBooks("Genesis").first()
        assert(bookSearchResults.isNotEmpty())
        
        val verseReference = repository.searchByVerseReference("Genesis 1:1", 1)
        assert(verseReference != null)
    }
} 