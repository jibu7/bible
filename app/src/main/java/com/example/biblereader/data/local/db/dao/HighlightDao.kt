package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.HighlightEntity
import com.example.biblereader.data.model.HighlightWithContext
import kotlinx.coroutines.flow.Flow

@Dao
interface HighlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace if highlighted again (e.g., change color)
    suspend fun insertHighlight(highlight: HighlightEntity): Long

    @Delete
    suspend fun deleteHighlight(highlight: HighlightEntity)

    @Query("DELETE FROM highlights WHERE verseId = :verseId")
    suspend fun deleteHighlightByVerseId(verseId: Int)

    @Query("SELECT * FROM highlights ORDER BY timestamp DESC")
    fun getAllHighlights(): Flow<List<HighlightEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM highlights WHERE verseId = :verseId LIMIT 1)")
    fun isVerseHighlighted(verseId: Int): Flow<Boolean>

    // You might want a query to get the highlight color for a specific verse
    @Query("SELECT * FROM highlights WHERE verseId = :verseId LIMIT 1")
    fun getHighlightForVerse(verseId: Int): Flow<HighlightEntity?>

    @Query("SELECT * FROM highlights")
    suspend fun getAllHighlightsList(): List<HighlightEntity> // For export

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Use REPLACE for import
    suspend fun insertHighlights(highlights: List<HighlightEntity>)

    @Query("DELETE FROM highlights")
    suspend fun deleteAllHighlights() // For import/reset

    // NEW: Get all highlights with their full context
    @Query("""
        SELECT
            hl.id as highlightId,
            hl.timestamp as highlightTimestamp,
            hl.colorHex as highlightColorHex,
            v.verseId as verseId,
            v.verseNumber as verseNumber,
            c.chapterId as chapterId,
            c.chapterNumber as chapterNumber,
            b.bookId as bookId,
            b.name as bookName,
            t.verseText as translatedText -- Changed t.translatedText to t.verseText
        FROM highlights hl
        INNER JOIN verses v ON hl.verseId = v.verseId
        INNER JOIN chapters c ON v.chapterId = c.chapterId
        INNER JOIN books b ON c.bookId = b.bookId
        INNER JOIN translations t ON v.verseId = t.verseId
        WHERE t.languageId = :languageId
        ORDER BY hl.timestamp DESC
    """)
    fun getAllHighlightsWithContext(languageId: Int): Flow<List<HighlightWithContext>>
}
