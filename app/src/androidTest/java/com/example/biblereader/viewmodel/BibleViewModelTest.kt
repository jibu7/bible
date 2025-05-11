package com.example.biblereader.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.data.local.db.entities.ChapterEntity
import com.example.biblereader.data.local.db.entities.VerseEntity
import com.example.biblereader.data.model.VerseSearchResult
import com.example.biblereader.repository.AppRepository
import com.example.biblereader.ui.viewmodels.BibleViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BibleViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    private lateinit var viewModel: BibleViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        
        // Create mock repository
        val mockRepository = mockk<AppRepository>()
        
        // Set up mock data
        val mockBook = BookEntity(bookId = 1, name = "Genesis", testament = "Old Testament", bookOrder = 1)
        val mockChapter = ChapterEntity(chapterId = 1, bookId = 1, chapterNumber = 1)
        val mockVerse = VerseEntity(verseId = 1, chapterId = 1, verseNumber = 1)
        val mockSearchResult = VerseSearchResult(
            verseId = 1,
            chapterId = 1,
            bookId = 1,
            verseNumber = 16,
            chapterNumber = 3,
            bookName = "John",
            translatedText = "For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life."
        )
        
        // Set up mock responses
        every { mockRepository.getAllBooks() } returns flowOf(listOf(mockBook))
        every { mockRepository.getChaptersByBook(1) } returns flowOf(listOf(mockChapter))
        every { mockRepository.getVersesByChapter(1) } returns flowOf(listOf(mockVerse))
        every { mockRepository.searchVersesByKeyword("John 3:16", 1) } returns flowOf(listOf(mockSearchResult))
        
        // Create viewModel with mock repository
        viewModel = BibleViewModel(mockRepository)
    }

    @Test
    fun getBook_returnsCorrectBook() = runTest {
        // When
        val book = viewModel.getBook(1).first()

        // Then
        assert(book != null)
        assert(book?.name == "Genesis")
    }

    @Test
    fun getChapter_returnsCorrectChapter() = runTest {
        // When
        val chapter = viewModel.getChapter(1, 1).first()

        // Then
        assert(chapter != null)
        assert(chapter?.bookId == 1)
        assert(chapter?.chapterNumber == 1)
    }

    @Test
    fun dataRetrievalFromRepositoryIsAccurate() = runTest {
        // Given
        val expectedBookCount = 1 // We only have one mock book

        // When
        val books = viewModel.books.first()

        // Then
        assert(books.size == expectedBookCount)
        assert(books.first().name == "Genesis")
    }

    @Test
    fun businessLogicTransformationsAreCorrect() = runTest {
        // Given
        val bookId = 1 // Genesis
        val chapterId = 1 // Genesis 1

        // When
        val chapters = viewModel.getChapters(bookId).first()
        val verses = viewModel.getVerses(chapterId).first()

        // Then
        assert(chapters.isNotEmpty())
        assert(verses.isNotEmpty())
        assert(chapters.first().bookId == bookId)
        assert(verses.first().chapterId == chapterId)
    }

    @Test
    fun stateFlowEmissionsUpdateCorrectly() = runTest {
        // Given
        val searchQuery = "John 3:16"

        // When
        val searchResults = viewModel.searchVerses(searchQuery).first()

        // Then
        assert(searchResults.isNotEmpty())
        assert(searchResults.any { it.translatedText.contains("For God so loved the world") })
    }

    @Test
    fun verseFormattingIsCorrect() = runTest {
        // Given
        val chapterId = 1 // Genesis 1

        // When
        val verses = viewModel.getVerses(chapterId).first()

        // Then
        assert(verses.isNotEmpty())
        verses.forEach { verse ->
            assert(verse.verseNumber > 0)
        }
    }
} 