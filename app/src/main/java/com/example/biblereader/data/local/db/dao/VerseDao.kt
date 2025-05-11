package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.VerseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VerseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Query("SELECT * FROM verses WHERE chapterId = :chapterId ORDER BY verseNumber")
    fun getVersesByChapter(chapterId: Int): Flow<List<VerseEntity>>

    @Query("SELECT * FROM verses WHERE verseId = :verseId") // Changed 'id' to 'verseId'
    suspend fun getVerseById(verseId: Int): VerseEntity?

    // Keyword search will likely be complex and might involve joins with TranslationEntity.
    // We'll refine this later.
    // Example:
    // @Query("SELECT v.* FROM verses v JOIN translations t ON v.verseId = t.verseId WHERE t.languageId = :languageId AND t.translatedText LIKE '%' || :query || '%' ORDER BY v.verseId") // Corrected v.id to v.verseId
    // fun searchVersesByKeyword(query: String, languageId: Int): Flow<List<VerseEntity>>

    // Verse reference search (e.g., "John 3:16") would require parsing the input and querying
    // across Book, Chapter, and Verse tables/DAOs, likely best handled in the Repository.
    
    @Query("DELETE FROM verses")
    suspend fun deleteAllVerses()
}
