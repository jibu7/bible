package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.TranslationEntity
import com.example.biblereader.data.model.VerseSearchResult
import kotlinx.coroutines.flow.Flow

@Dao
interface TranslationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTranslations(translations: List<TranslationEntity>)

    @Query("SELECT verseText FROM translations WHERE verseId = :verseId AND languageId = :languageId") // Changed translatedText to verseText
    suspend fun getTranslationTextForVerse(verseId: Int, languageId: Int): String?

    @Query("SELECT * FROM translations WHERE verseId = :verseId AND languageId = :languageId")
    suspend fun getTranslationForVerse(verseId: Int, languageId: Int): TranslationEntity?

     // Optimized query to get all translations for a chapter in a specific language
    @Query("""
        SELECT t.* FROM translations t
        INNER JOIN verses v ON t.verseId = v.verseId
        WHERE v.chapterId = :chapterId AND t.languageId = :languageId
        ORDER BY v.verseNumber
    """)
    fun getTranslationsForChapter(chapterId: Int, languageId: Int): Flow<List<TranslationEntity>>

    // Keyword search within a specific language
    @Query("""
        SELECT
            t.verseId as verseId,
            v.chapterId as chapterId,
            c.bookId as bookId,
            v.verseNumber as verseNumber,
            c.chapterNumber as chapterNumber,
            b.name as bookName,
            t.verseText as translatedText -- Changed t.translatedText to t.verseText
        FROM translations t
        INNER JOIN verses v ON t.verseId = v.verseId
        INNER JOIN chapters c ON v.chapterId = c.chapterId
        INNER JOIN books b ON c.bookId = b.bookId
        WHERE t.languageId = :languageId AND t.verseText LIKE '%' || :query || '%' -- Changed t.translatedText to t.verseText
        ORDER BY b.bookId, c.chapterNumber, v.verseNumber
    """)
    fun searchTranslationsByKeyword(query: String, languageId: Int): Flow<List<VerseSearchResult>>

    @Query("DELETE FROM translations")
    suspend fun deleteAllTranslations()
}
