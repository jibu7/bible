package com.example.biblereader.util

import com.example.biblereader.data.local.db.AppDatabase
import com.example.biblereader.data.local.db.entities.*
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TestDataSetup @Inject constructor(
    private val database: AppDatabase
) {
    fun setupTestData() = runBlocking {
        // Clear existing data
        clearAllTables()
        
        // Insert test books
        val genesis = BookEntity(bookId = 1, name = "Genesis", testament = "Old", bookOrder = 1)
        val exodus = BookEntity(bookId = 2, name = "Exodus", testament = "Old", bookOrder = 2)
        val john = BookEntity(bookId = 66, name = "John", testament = "New", bookOrder = 66)
        val revelation = BookEntity(bookId = 66, name = "Revelation", testament = "New", bookOrder = 66)
        
        database.bookDao().insertBooks(listOf(genesis, exodus, john, revelation))
        
        // Insert test chapters
        val genesisChapter1 = ChapterEntity(chapterId = 1, bookId = 1, chapterNumber = 1)
        val genesisChapter2 = ChapterEntity(chapterId = 2, bookId = 1, chapterNumber = 2)
        val johnChapter3 = ChapterEntity(chapterId = 1000, bookId = 66, chapterNumber = 3)
        
        database.chapterDao().insertChapters(listOf(genesisChapter1, genesisChapter2, johnChapter3))
        
        // Insert test verses
        val genesisVerse1 = VerseEntity(verseId = 1, chapterId = 1, verseNumber = 1)
        val genesisVerse2 = VerseEntity(verseId = 2, chapterId = 1, verseNumber = 2)
        val johnVerse16 = VerseEntity(verseId = 1000, chapterId = 1000, verseNumber = 16)
        
        database.verseDao().insertVerses(listOf(genesisVerse1, genesisVerse2, johnVerse16))
        
        // Insert test languages
        val english = LanguageEntity(languageId = 1, languageName = "English", languageCode = "en")
        val spanish = LanguageEntity(languageId = 2, languageName = "Spanish", languageCode = "es")
        
        database.languageDao().insertLanguages(listOf(english, spanish))
        
        // Insert test translations
        val genesisVerse1En = TranslationEntity(
            translationId = 1, 
            verseId = 1, 
            languageId = 1, 
            languageCode = "en",
            verseText = "In the beginning God created the heaven and the earth."
        )
        val genesisVerse2En = TranslationEntity(
            translationId = 2, 
            verseId = 2, 
            languageId = 1, 
            languageCode = "en",
            verseText = "And the earth was without form, and void; and darkness was upon the face of the deep."
        )
        val johnVerse16En = TranslationEntity(
            translationId = 1000, 
            verseId = 1000, 
            languageId = 1, 
            languageCode = "en",
            verseText = "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."
        )
        
        database.translationDao().insertTranslations(listOf(genesisVerse1En, genesisVerse2En, johnVerse16En))
    }
    
    private suspend fun clearAllTables() {
        // Delete data in reverse order of dependencies
        database.bookmarkDao().deleteAllBookmarks()
        database.highlightDao().deleteAllHighlights()
        database.translationDao().deleteAllTranslations()
        database.verseDao().deleteAllVerses()
        database.chapterDao().deleteAllChapters()
        database.bookDao().deleteAllBooks()
        database.languageDao().deleteAllLanguages()
        database.headingDao().deleteAllHeadings()
    }
} 