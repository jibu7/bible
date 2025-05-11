package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.ChapterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChapterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<ChapterEntity>)

    @Query("SELECT * FROM chapters WHERE bookId = :bookId ORDER BY chapterNumber")
    fun getChaptersByBook(bookId: Int): Flow<List<ChapterEntity>>

    @Query("SELECT * FROM chapters WHERE chapterId = :chapterId")
    suspend fun getChapterById(chapterId: Int): ChapterEntity?

    // Add other necessary queries
    
    @Query("DELETE FROM chapters")
    suspend fun deleteAllChapters()
}
