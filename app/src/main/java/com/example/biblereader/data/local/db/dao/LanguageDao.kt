package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.LanguageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLanguages(languages: List<LanguageEntity>)

    @Query("SELECT * FROM languages ORDER BY languageName")
    fun getAllLanguages(): Flow<List<LanguageEntity>>

    @Query("SELECT * FROM languages WHERE languageId = :languageId")
    suspend fun getLanguageById(languageId: Int): LanguageEntity?

    @Query("SELECT * FROM languages WHERE languageCode = :languageCode")
    suspend fun getLanguageByCode(languageCode: String): LanguageEntity?

    // Add other necessary queries
    
    @Query("DELETE FROM languages")
    suspend fun deleteAllLanguages()
}
