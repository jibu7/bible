package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "languages",
    indices = [Index(value = ["languageCode"], unique = true)] // Ensure language codes are unique
)
data class LanguageEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "languageId") // Map to the actual column name
    val languageId: Int = 0, // Default value for autoGenerate
    @ColumnInfo(name = "languageCode")
    val languageCode: String,
    @ColumnInfo(name = "languageName") // Map to the actual column name
    val languageName: String // Renamed from 'name'
)
