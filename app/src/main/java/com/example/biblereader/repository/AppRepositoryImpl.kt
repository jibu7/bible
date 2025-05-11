package com.example.biblereader.repository

import com.example.biblereader.data.local.db.dao.*
import com.example.biblereader.data.local.db.entities.*
import com.example.biblereader.data.model.VerseSearchResult
import com.example.biblereader.data.model.BookmarkWithContext
import com.example.biblereader.data.model.HighlightWithContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject // Import if using Hilt/Dagger
import javax.inject.Singleton // Import if using Hilt/Dagger

// Use @Singleton if you want a single instance provided by Hilt/Dagger
// @Singleton
@Singleton
class AppRepositoryImpl @Inject constructor ( // Add @Inject here
    private val bookDao: BookDao,
    private val chapterDao: ChapterDao,
    private val verseDao: VerseDao,
    private val headingDao: HeadingDao,
    private val languageDao: LanguageDao,
    private val translationDao: TranslationDao,
    private val bookmarkDao: BookmarkDao,
    private val highlightDao: HighlightDao
) : AppRepository {

    // --- Bible Data ---

    override fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()
    override fun getBooksByTestament(testament: String): Flow<List<BookEntity>> = bookDao.getBooksByTestament(testament)
    override suspend fun getBookById(bookId: Int): BookEntity? = withContext(Dispatchers.IO) { bookDao.getBookById(bookId) }

    override fun getChaptersByBook(bookId: Int): Flow<List<ChapterEntity>> = chapterDao.getChaptersByBook(bookId)
    override suspend fun getChapterById(chapterId: Int): ChapterEntity? = withContext(Dispatchers.IO) { chapterDao.getChapterById(chapterId) }

    override fun getVersesByChapter(chapterId: Int): Flow<List<VerseEntity>> = verseDao.getVersesByChapter(chapterId)
    override suspend fun getVerseById(verseId: Int): VerseEntity? = withContext(Dispatchers.IO) { verseDao.getVerseById(verseId) }

    override fun getHeadingsByChapter(chapterId: Int): Flow<List<HeadingEntity>> = headingDao.getHeadingsByChapter(chapterId)

    // --- Language & Translation ---

    override fun getAllLanguages(): Flow<List<LanguageEntity>> = languageDao.getAllLanguages()
    override suspend fun getLanguageById(languageId: Int): LanguageEntity? = withContext(Dispatchers.IO) { languageDao.getLanguageById(languageId) }
    override suspend fun getLanguageByCode(languageCode: String): LanguageEntity? = withContext(Dispatchers.IO) { languageDao.getLanguageByCode(languageCode) }

    override suspend fun getTranslationTextForVerse(verseId: Int, languageId: Int): String? =
        withContext(Dispatchers.IO) { translationDao.getTranslationTextForVerse(verseId, languageId) }

    override fun getTranslationsForChapter(chapterId: Int, languageId: Int): Flow<List<TranslationEntity>> =
        translationDao.getTranslationsForChapter(chapterId, languageId)

    // --- User Data (Bookmarks & Highlights) ---

    override fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    override suspend fun addBookmark(verseId: Int): Unit = withContext(Dispatchers.IO) {
        val bookmark = BookmarkEntity(verseId = verseId)
        bookmarkDao.insertBookmark(bookmark)
    }

    override suspend fun removeBookmark(verseId: Int) = withContext(Dispatchers.IO) {
        bookmarkDao.deleteBookmarkByVerseId(verseId)
    }

    override fun isVerseBookmarked(verseId: Int): Flow<Boolean> = bookmarkDao.isVerseBookmarked(verseId)

    override suspend fun exportBookmarks(): List<BookmarkEntity> = withContext(Dispatchers.IO) {
        bookmarkDao.getAllBookmarksList()
    }

    override suspend fun importBookmarks(bookmarks: List<BookmarkEntity>) = withContext(Dispatchers.IO) {
        // Optional: Clear existing before import? Or just insert/replace?
        // bookmarkDao.deleteAllBookmarks()
        bookmarkDao.insertBookmarks(bookmarks)
    }


    override fun getAllHighlights(): Flow<List<HighlightEntity>> = highlightDao.getAllHighlights()

    override suspend fun addHighlight(verseId: Int, colorHex: String?): Unit = withContext(Dispatchers.IO) {
        val highlight = HighlightEntity(verseId = verseId, colorHex = colorHex)
        highlightDao.insertHighlight(highlight)
    }

    override suspend fun removeHighlight(verseId: Int) = withContext(Dispatchers.IO) {
        highlightDao.deleteHighlightByVerseId(verseId)
    }

    override fun isVerseHighlighted(verseId: Int): Flow<Boolean> = highlightDao.isVerseHighlighted(verseId)

    override fun getHighlightForVerse(verseId: Int): Flow<HighlightEntity?> = highlightDao.getHighlightForVerse(verseId)


    override suspend fun exportHighlights(): List<HighlightEntity> = withContext(Dispatchers.IO) {
        highlightDao.getAllHighlightsList()
    }

    override suspend fun importHighlights(highlights: List<HighlightEntity>) = withContext(Dispatchers.IO) {
        // Optional: Clear existing before import?
        // highlightDao.deleteAllHighlights()
        highlightDao.insertHighlights(highlights)
    }

    // --- Search ---

    override fun searchBooks(query: String): Flow<List<BookEntity>> = bookDao.searchBooks(query)

    override fun searchVersesByKeyword(query: String, languageId: Int): Flow<List<VerseSearchResult>> =
        translationDao.searchTranslationsByKeyword(query, languageId)

    override suspend fun searchByVerseReference(reference: String, languageId: Int): TranslationEntity? = withContext(Dispatchers.IO) {
        // TODO: Implement verse reference parsing and searching logic
        // 1. Parse the reference string (e.g., "John 3:16") into book name/abbr, chapter, verse.
        //    - Handle variations like "1 John 3:16", "Genesis 1".
        // 2. Find the book using BookDao (searchBooks or a dedicated lookup).
        // 3. Find the chapter using ChapterDao (based on bookId and chapterNumber).
        // 4. Find the verse using VerseDao (based on chapterId and verseNumber).
        // 5. Get the specific translation using TranslationDao (based on verseId and languageId).
        // This requires careful parsing and error handling (e.g., book/chapter/verse not found).
        // Consider creating a helper class/function for parsing the reference.
        // Example (highly simplified, needs proper implementation):
        /*
        val parts = parseReference(reference) // Implement this parsing function
        if (parts == null) return@withContext null

        val book = bookDao.findBookByNameOrAbbr(parts.bookQuery) // Needs DAO method
        if (book == null) return@withContext null

        val chapter = chapterDao.findChapterByNumber(book.id, parts.chapterNumber) // Needs DAO method
        if (chapter == null) return@withContext null

        val verse = verseDao.findVerseByNumber(chapter.id, parts.verseNumber) // Needs DAO method
        if (verse == null) return@withContext null

        return@withContext translationDao.getTranslationForVerse(verse.id, languageId)
         */
         println("Warning: searchByVerseReference is not implemented yet.")
        return@withContext null // Placeholder
    }

    // Implement NEW methods
    override fun getAllBookmarksWithContext(languageId: Int): Flow<List<BookmarkWithContext>> =
        bookmarkDao.getAllBookmarksWithContext(languageId)

    override fun getAllHighlightsWithContext(languageId: Int): Flow<List<HighlightWithContext>> =
        highlightDao.getAllHighlightsWithContext(languageId)
}
