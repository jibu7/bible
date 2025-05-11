package com.example.biblereader.data.model // Or data.local.db.model

import androidx.room.ColumnInfo
import com.example.biblereader.data.local.db.entities.BookmarkEntity // Keep original entity if needed? Maybe not.
import com.example.biblereader.data.local.db.entities.HighlightEntity

// Data class to hold enriched verse search results
data class VerseSearchResult(
    @ColumnInfo(name = "verseId") val verseId: Int,
    @ColumnInfo(name = "chapterId") val chapterId: Int,
    @ColumnInfo(name = "bookId") val bookId: Int, // Optional, but good to have
    @ColumnInfo(name = "verseNumber") val verseNumber: Int,
    @ColumnInfo(name = "chapterNumber") val chapterNumber: Int,
    @ColumnInfo(name = "bookName") val bookName: String,
    @ColumnInfo(name = "translatedText") val translatedText: String
) {
    // Helper property to display the reference string
    val reference: String
        get() = "$bookName $chapterNumber:$verseNumber"
}

// Combined data for displaying a bookmark
data class BookmarkWithContext(
    // Include fields from VerseSearchResult as they are the same context
    @ColumnInfo(name = "verseId") val verseId: Int,
    @ColumnInfo(name = "chapterId") val chapterId: Int,
    @ColumnInfo(name = "bookId") val bookId: Int,
    @ColumnInfo(name = "verseNumber") val verseNumber: Int,
    @ColumnInfo(name = "chapterNumber") val chapterNumber: Int,
    @ColumnInfo(name = "bookName") val bookName: String,
    @ColumnInfo(name = "translatedText") val translatedText: String,
    // Add bookmark specific fields if any (e.g., timestamp from BookmarkEntity)
    @ColumnInfo(name = "bookmarkId") val bookmarkId: Int,
    @ColumnInfo(name = "bookmarkTimestamp") val bookmarkTimestamp: Long
) {
    val reference: String get() = "$bookName $chapterNumber:$verseNumber"
}

// Combined data for displaying a highlight
data class HighlightWithContext(
    // Include fields from VerseSearchResult
    @ColumnInfo(name = "verseId") val verseId: Int,
    @ColumnInfo(name = "chapterId") val chapterId: Int,
    @ColumnInfo(name = "bookId") val bookId: Int,
    @ColumnInfo(name = "verseNumber") val verseNumber: Int,
    @ColumnInfo(name = "chapterNumber") val chapterNumber: Int,
    @ColumnInfo(name = "bookName") val bookName: String,
    @ColumnInfo(name = "translatedText") val translatedText: String,
    // Add highlight specific fields
    @ColumnInfo(name = "highlightId") val highlightId: Int,
    @ColumnInfo(name = "highlightTimestamp") val highlightTimestamp: Long,
    @ColumnInfo(name = "highlightColorHex") val highlightColorHex: String?
) {
     val reference: String get() = "$bookName $chapterNumber:$verseNumber"
}
