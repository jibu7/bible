package com.example.biblereader.repository

import com.example.biblereader.data.local.db.dao.*
import com.example.biblereader.data.local.db.entities.*
import com.example.biblereader.data.model.BookmarkWithContext
import com.example.biblereader.data.model.HighlightWithContext
import com.example.biblereader.data.model.VerseSearchResult
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AppRepositoryImplTest {

    private lateinit var repository: AppRepositoryImpl
    private val bookDao: BookDao = mockk()
    private val chapterDao: ChapterDao = mockk()
    private val verseDao: VerseDao = mockk()
    private val headingDao: HeadingDao = mockk()
    private val languageDao: LanguageDao = mockk()
    private val translationDao: TranslationDao = mockk()
    private val bookmarkDao: BookmarkDao = mockk()
    private val highlightDao: HighlightDao = mockk()

    @Before
    fun setup() {
        repository = AppRepositoryImpl(
            bookDao = bookDao,
            chapterDao = chapterDao,
            verseDao = verseDao,
            headingDao = headingDao,
            languageDao = languageDao,
            translationDao = translationDao,
            bookmarkDao = bookmarkDao,
            highlightDao = highlightDao
        )
    }

    @Test
    fun `getAllBooks returns flow of books`() = runTest {
        // Arrange
        val expectedBooks = listOf(
            BookEntity(bookId = 1, name = "Genesis", testament = "Old Testament", bookOrder = 1),
            BookEntity(bookId = 2, name = "Exodus", testament = "Old Testament", bookOrder = 2)
        )
        every { bookDao.getAllBooks() } returns flowOf(expectedBooks)

        // Act
        val result = repository.getAllBooks()

        // Assert
        assertNotNull(result)
        result.collect { books ->
            assertEquals(expectedBooks, books)
        }
    }

    @Test
    fun `getBooksByTestament returns flow of books for the given testament`() = runTest {
        // Arrange
        val testament = "Old Testament"
        val expectedBooks = listOf(
            BookEntity(bookId = 1, name = "Genesis", testament = testament, bookOrder = 1),
            BookEntity(bookId = 2, name = "Exodus", testament = testament, bookOrder = 2)
        )
        every { bookDao.getBooksByTestament(testament) } returns flowOf(expectedBooks)

        // Act
        val result = repository.getBooksByTestament(testament)

        // Assert
        assertNotNull(result)
        result.collect { books ->
            assertEquals(expectedBooks, books)
        }
    }

    @Test
    fun `getBookById returns the correct book`() = runTest {
        // Arrange
        val bookId = 1
        val expectedBook = BookEntity(bookId = bookId, name = "Genesis", testament = "Old Testament", bookOrder = 1)
        coEvery { bookDao.getBookById(bookId) } returns expectedBook

        // Act
        val result = repository.getBookById(bookId)

        // Assert
        assertNotNull(result)
        assertEquals(expectedBook, result)
    }

    @Test
    fun `getAllLanguages returns flow of languages`() = runTest {
        // Arrange
        val expectedLanguages = listOf(
            LanguageEntity(languageId = 1, languageName = "English", languageCode = "en"),
            LanguageEntity(languageId = 2, languageName = "Kinyarwanda", languageCode = "rw")
        )
        every { languageDao.getAllLanguages() } returns flowOf(expectedLanguages)

        // Act
        val result = repository.getAllLanguages()

        // Assert
        assertNotNull(result)
        result.collect { languages ->
            assertEquals(expectedLanguages, languages)
        }
    }

    @Test
    fun `addBookmark calls bookmarkDao insertBookmark`() = runTest {
        // Arrange
        val verseId = 1
        coEvery { bookmarkDao.insertBookmark(any()) } returns 1L

        // Act
        repository.addBookmark(verseId)

        // Assert
        coVerify { bookmarkDao.insertBookmark(match { it.verseId == verseId }) }
    }

    @Test
    fun `removeBookmark calls bookmarkDao deleteBookmarkByVerseId`() = runTest {
        // Arrange
        val verseId = 1
        coEvery { bookmarkDao.deleteBookmarkByVerseId(verseId) } returns Unit

        // Act
        repository.removeBookmark(verseId)

        // Assert
        coVerify { bookmarkDao.deleteBookmarkByVerseId(verseId) }
    }

    @Test
    fun `isVerseBookmarked returns flow of boolean`() = runTest {
        // Arrange
        val verseId = 1
        every { bookmarkDao.isVerseBookmarked(verseId) } returns flowOf(true)

        // Act
        val result = repository.isVerseBookmarked(verseId)

        // Assert
        assertNotNull(result)
        result.collect { isBookmarked ->
            assertTrue(isBookmarked)
        }
    }

    @Test
    fun `addHighlight calls highlightDao insertHighlight`() = runTest {
        // Arrange
        val verseId = 1
        val colorHex = "#FF0000"
        coEvery { highlightDao.insertHighlight(any()) } returns 1L

        // Act
        repository.addHighlight(verseId, colorHex)

        // Assert
        coVerify { highlightDao.insertHighlight(match { it.verseId == verseId && it.colorHex == colorHex }) }
    }

    @Test
    fun `removeHighlight calls highlightDao deleteHighlightByVerseId`() = runTest {
        // Arrange
        val verseId = 1
        coEvery { highlightDao.deleteHighlightByVerseId(verseId) } returns Unit

        // Act
        repository.removeHighlight(verseId)

        // Assert
        coVerify { highlightDao.deleteHighlightByVerseId(verseId) }
    }

    @Test
    fun `isVerseHighlighted returns flow of boolean`() = runTest {
        // Arrange
        val verseId = 1
        every { highlightDao.isVerseHighlighted(verseId) } returns flowOf(true)

        // Act
        val result = repository.isVerseHighlighted(verseId)

        // Assert
        assertNotNull(result)
        result.collect { isHighlighted ->
            assertTrue(isHighlighted)
        }
    }
} 