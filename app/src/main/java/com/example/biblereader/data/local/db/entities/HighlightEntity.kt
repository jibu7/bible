package com.example.biblereader.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "highlights",
    foreignKeys = [ForeignKey(
        entity = VerseEntity::class,
        parentColumns = ["verseId"],
        childColumns = ["verseId"],
        onDelete = ForeignKey.CASCADE
    )],
    // Ensure a verse can only be highlighted once (or manage multiple highlights differently)
    indices = [Index(value = ["verseId"], unique = true)]
)
data class HighlightEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val verseId: Int,
    val colorHex: String? = null, // Optional: Store color as hex string (e.g., "#FFFF00")
    val timestamp: Long = System.currentTimeMillis()
)
