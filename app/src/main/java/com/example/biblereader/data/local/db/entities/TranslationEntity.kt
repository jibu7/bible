package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translations",
    foreignKeys = [
        ForeignKey(
            entity = VerseEntity::class,
            parentColumns = ["verseId"], // Corrected parent column
            childColumns = ["verseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LanguageEntity::class,
            parentColumns = ["languageId"], // Corrected parent column
            childColumns = ["languageId"],
            // Changed onDelete to RESTRICT as per schema
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [
        Index(value = ["verseId"]),
        Index(value = ["languageId"]),
        Index(value = ["verseId", "languageId"], unique = true) // Matches schema
    ]
)
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "translationId") // Map to the actual column name
    val translationId: Int = 0, // Default value for autoGenerate
    val verseId: Int,
    val languageId: Int,
    val languageCode: String, // Added field
    @ColumnInfo(name = "verseText") // Map to the actual column name
    val verseText: String // Renamed from translatedText
)
