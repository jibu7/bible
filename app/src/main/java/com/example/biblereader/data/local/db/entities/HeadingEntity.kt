package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "headings",
    foreignKeys = [ForeignKey(
        entity = ChapterEntity::class,
        parentColumns = ["chapterId"], // Corrected parent column
        childColumns = ["chapterId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["chapterId"]), // Keep index for lookups
        Index(value = ["chapterId", "headingOrder"], unique = true) // Added unique constraint
    ]
)
data class HeadingEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "headingId") // Map to the actual column name
    val headingId: Int = 0, // Default value for autoGenerate
    val chapterId: Int,
    val headingOrder: Int, // Added field
    val headingText: String
    // Removed startVerseNumber and endVerseNumber
)
