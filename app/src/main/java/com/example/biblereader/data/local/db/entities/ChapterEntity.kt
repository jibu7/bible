package com.example.biblereader.data.local.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "chapters",
    foreignKeys = [ForeignKey(
        entity = BookEntity::class,
        parentColumns = ["bookId"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["bookId"]),
        Index(value = ["bookId", "chapterNumber"], unique = true)
    ]
)
data class ChapterEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "chapterId")
    val chapterId: Int? = null,
    
    val bookId: Int,
    val chapterNumber: Int
)
