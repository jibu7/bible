package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.HeadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HeadingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHeadings(headings: List<HeadingEntity>)

    @Query("SELECT * FROM headings WHERE chapterId = :chapterId ORDER BY headingOrder")
    fun getHeadingsByChapter(chapterId: Int): Flow<List<HeadingEntity>>

    // Add other necessary queries
    
    @Query("DELETE FROM headings")
    suspend fun deleteAllHeadings()
}
