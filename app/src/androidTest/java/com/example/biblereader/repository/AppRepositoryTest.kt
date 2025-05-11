package com.example.biblereader.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.repository.AppRepository
import com.example.biblereader.util.TestDataSetup
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var repository: AppRepository
    
    @Inject
    lateinit var testDataSetup: TestDataSetup

    @Before
    fun setup() {
        hiltRule.inject()
        testDataSetup.setupTestData()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun getBookById_returnsCorrectBook() = runBlocking {
        // When
        val book = repository.getBookById(1)

        // Then
        assert(book != null)
        assert(book?.name == "Genesis")
    }

    @Test
    fun getChapterById_returnsCorrectChapter() = runBlocking {
        // When
        val chapter = repository.getChapterById(1)

        // Then
        assert(chapter != null)
        assert(chapter?.bookId == 1)
        assert(chapter?.chapterNumber == 1)
    }

    @Test
    fun getAllBooks_returnsAllBibleBooks() = runBlocking {
        // Given
        val expectedBookCount = 4 // We only inserted 4 books in the test data

        // When
        val books = repository.getAllBooks().first()

        // Then
        assert(books.size == expectedBookCount)
        assert(books.first().name == "Genesis")
        assert(books.last().name == "Revelation")
    }

    @Test
    fun getChaptersByBook_retrievesChaptersForSpecificBook() = runBlocking {
        // Given
        val bookId = 1 // Genesis

        // When
        val chapters = repository.getChaptersByBook(bookId).first()

        // Then
        assert(chapters.isNotEmpty())
        assert(chapters.first().bookId == bookId)
        assert(chapters.size == 2) // We inserted 2 chapters for Genesis
    }

    @Test
    fun getVersesByChapter_fetchesVersesForGivenChapter() = runBlocking {
        // Given
        val chapterId = 1 // Genesis 1

        // When
        val verses = repository.getVersesByChapter(chapterId).first()

        // Then
        assert(verses.isNotEmpty())
        assert(verses.first().chapterId == chapterId)
    }

    @Test
    fun searchVersesByKeyword_handlesKeywordSearch() = runBlocking {
        // Given
        val query = "loved"
        val languageId = 1 // English

        // When
        val results = repository.searchVersesByKeyword(query, languageId).first()

        // Then
        assert(results.isNotEmpty())
        assert(results.any { it.translatedText.contains(query, ignoreCase = true) })
    }

    @Test
    fun searchBooks_handlesBookNameSearch() = runBlocking {
        // Given
        val query = "Genesis"

        // When
        val results = repository.searchBooks(query).first()

        // Then
        assert(results.isNotEmpty())
        assert(results.any { it.name.contains(query, ignoreCase = true) })
    }
} 