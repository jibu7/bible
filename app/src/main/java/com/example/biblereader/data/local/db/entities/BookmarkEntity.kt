package com.example.biblereader.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "bookmarks",
    foreignKeys = [ForeignKey(
        entity = VerseEntity::class,
        parentColumns = ["verseId"],
        childColumns = ["verseId"],
        onDelete = ForeignKey.CASCADE
    )],
    // Ensure a verse can only be bookmarked once
    indices = [Index(value = ["verseId"], unique = true)]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val verseId: Int,
    val timestamp: Long = System.currentTimeMillis() // Store as Long (Unix timestamp)
)
