package com.example.biblereader.repository

import com.example.biblereader.data.local.db.entities.*
import com.example.biblereader.data.model.VerseSearchResult
import com.example.biblereader.data.model.BookmarkWithContext
import com.example.biblereader.data.model.HighlightWithContext
import com.example.biblereader.data.local.db.dao.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface AppRepository {

    // --- Bible Data ---

    fun getAllBooks(): Flow<List<BookEntity>>
    fun getBooksByTestament(testament: String): Flow<List<BookEntity>>
    suspend fun getBookById(bookId: Int): BookEntity?

    fun getChaptersByBook(bookId: Int): Flow<List<ChapterEntity>>
    suspend fun getChapterById(chapterId: Int): ChapterEntity?

    fun getVersesByChapter(chapterId: Int): Flow<List<VerseEntity>>
    suspend fun getVerseById(verseId: Int): VerseEntity?

    fun getHeadingsByChapter(chapterId: Int): Flow<List<HeadingEntity>>

    // --- Language & Translation ---

    fun getAllLanguages(): Flow<List<LanguageEntity>>
    suspend fun getLanguageById(languageId: Int): LanguageEntity?
    suspend fun getLanguageByCode(languageCode: String): LanguageEntity?

    // Get translated text for a single verse
    suspend fun getTranslationTextForVerse(verseId: Int, languageId: Int): String?

    // Get all translated verses for a chapter in a specific language
    fun getTranslationsForChapter(chapterId: Int, languageId: Int): Flow<List<TranslationEntity>>

    // --- User Data (Bookmarks & Highlights) ---

    fun getAllBookmarks(): Flow<List<BookmarkEntity>>
    suspend fun addBookmark(verseId: Int)
    suspend fun removeBookmark(verseId: Int)
    fun isVerseBookmarked(verseId: Int): Flow<Boolean>
    suspend fun exportBookmarks(): List<BookmarkEntity> // For export feature
    suspend fun importBookmarks(bookmarks: List<BookmarkEntity>) // For import feature

    fun getAllHighlights(): Flow<List<HighlightEntity>>
    suspend fun addHighlight(verseId: Int, colorHex: String? = null)
    suspend fun removeHighlight(verseId: Int)
    fun isVerseHighlighted(verseId: Int): Flow<Boolean>
    fun getHighlightForVerse(verseId: Int): Flow<HighlightEntity?>
    suspend fun exportHighlights(): List<HighlightEntity> // For export feature
    suspend fun importHighlights(highlights: List<HighlightEntity>) // For import feature

    // NEW methods returning context
    fun getAllBookmarksWithContext(languageId: Int): Flow<List<BookmarkWithContext>>
    fun getAllHighlightsWithContext(languageId: Int): Flow<List<HighlightWithContext>>

    // Update export/import signatures if they should use the context objects, or keep using base entities
    // --- Search ---

    // Search books by name or abbreviation
    fun searchBooks(query: String): Flow<List<BookEntity>>

    // Search verses by keyword in a specific language
    fun searchVersesByKeyword(query: String, languageId: Int): Flow<List<VerseSearchResult>>

    // Search by verse reference (e.g., "John 3:16") - Complex logic needed here
    suspend fun searchByVerseReference(reference: String, languageId: Int): TranslationEntity? // Or maybe return Verse/Chapter/Book info?

}
