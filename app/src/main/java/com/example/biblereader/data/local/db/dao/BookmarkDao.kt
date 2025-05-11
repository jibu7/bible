package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.BookmarkEntity
import com.example.biblereader.data.model.BookmarkWithContext
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignore if already bookmarked
    suspend fun insertBookmark(bookmark: BookmarkEntity): Long // Returns row ID or -1 if ignored

    @Delete
    suspend fun deleteBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE verseId = :verseId")
    suspend fun deleteBookmarkByVerseId(verseId: Int)

    @Query("SELECT * FROM bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarks WHERE verseId = :verseId LIMIT 1)")
    fun isVerseBookmarked(verseId: Int): Flow<Boolean> // Use Flow to observe changes

     @Query("SELECT * FROM bookmarks")
    suspend fun getAllBookmarksList(): List<BookmarkEntity> // For export

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Use REPLACE for import
    suspend fun insertBookmarks(bookmarks: List<BookmarkEntity>)

    @Query("DELETE FROM bookmarks")
    suspend fun deleteAllBookmarks() // For import/reset

    @Query("""
        SELECT
            b.id AS bookmarkId,
            b.timestamp AS bookmarkTimestamp,
            v.verseId AS verseId,
            v.verseNumber AS verseNumber,
            c.chapterId AS chapterId,
            c.chapterNumber AS chapterNumber,
            bk.bookId AS bookId,
            bk.name AS bookName,
            t.verseText AS translatedText
        FROM bookmarks b
        JOIN verses v ON b.verseId = v.verseId
        JOIN chapters c ON v.chapterId = c.chapterId
        JOIN books bk ON c.bookId = bk.bookId
        JOIN translations t ON v.verseId = t.verseId
        WHERE t.languageId = :languageId
        ORDER BY b.timestamp DESC
    """)
    fun getAllBookmarksWithContext(languageId: Int): Flow<List<BookmarkWithContext>>
}
