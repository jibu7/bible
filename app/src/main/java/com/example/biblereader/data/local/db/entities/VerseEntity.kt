package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [ForeignKey(
        entity = ChapterEntity::class,
        parentColumns = ["chapterId"], // Corrected parent column
        childColumns = ["chapterId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["chapterId"]),
        Index(value = ["chapterId", "verseNumber"], unique = true) // Added unique constraint
    ]
)
data class VerseEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "verseId") // Map to the actual column name
    val verseId: Int = 0, // Default value for autoGenerate
    val chapterId: Int,
    val verseNumber: Int
)
