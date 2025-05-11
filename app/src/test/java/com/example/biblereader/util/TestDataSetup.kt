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
        val genesis = BookEntity(id = 1, name = "Genesis", testament = "Old", abbreviation = "Gen")
        val exodus = BookEntity(id = 2, name = "Exodus", testament = "Old", abbreviation = "Exo")
        val john = BookEntity(id = 66, name = "John", testament = "New", abbreviation = "Jhn")
        val revelation = BookEntity(id = 66, name = "Revelation", testament = "New", abbreviation = "Rev")
        
        database.bookDao().insertBooks(listOf(genesis, exodus, john, revelation))
        
        // Insert test chapters
        val genesisChapter1 = ChapterEntity(id = 1, bookId = 1, chapterNumber = 1)
        val genesisChapter2 = ChapterEntity(id = 2, bookId = 1, chapterNumber = 2)
        val johnChapter3 = ChapterEntity(id = 1000, bookId = 66, chapterNumber = 3)
        
        database.chapterDao().insertChapters(listOf(genesisChapter1, genesisChapter2, johnChapter3))
        
        // Insert test verses
        val genesisVerse1 = VerseEntity(id = 1, chapterId = 1, verseNumber = 1)
        val genesisVerse2 = VerseEntity(id = 2, chapterId = 1, verseNumber = 2)
        val johnVerse16 = VerseEntity(id = 1000, chapterId = 1000, verseNumber = 16)
        
        database.verseDao().insertVerses(listOf(genesisVerse1, genesisVerse2, johnVerse16))
        
        // Insert test languages
        val english = LanguageEntity(id = 1, name = "English", code = "en")
        val spanish = LanguageEntity(id = 2, name = "Spanish", code = "es")
        
        database.languageDao().insertLanguages(listOf(english, spanish))
        
        // Insert test translations
        val genesisVerse1En = TranslationEntity(
            id = 1, 
            verseId = 1, 
            languageId = 1, 
            translatedText = "In the beginning God created the heaven and the earth."
        )
        val genesisVerse2En = TranslationEntity(
            id = 2, 
            verseId = 2, 
            languageId = 1, 
            translatedText = "And the earth was without form, and void; and darkness was upon the face of the deep."
        )
        val johnVerse16En = TranslationEntity(
            id = 1000, 
            verseId = 1000, 
            languageId = 1, 
            translatedText = "For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life."
        )
        
        database.translationDao().insertTranslations(listOf(genesisVerse1En, genesisVerse2En, johnVerse16En))
    }
    
    private suspend fun clearAllTables() {
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