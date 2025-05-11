package com.example.biblereader.ui.model

// Represents items to be displayed in the verse list (verses and headings)
sealed interface DisplayItem {
    data class VerseDisplay(
        val verseId: Int,
        val verseNumber: Int,
        val text: String,
        // Add status flags
        val isBookmarked: Boolean = false,
        val isHighlighted: Boolean = false,
        val highlightColor: String? = null // Keep for potential future use
    ) : DisplayItem

    data class HeadingDisplay(
        val headingId: Int, // Use HeadingEntity's ID
        val text: String
    ) : DisplayItem
}

// Data class to hold chapter reference details for the App Bar
data class ChapterReference(
    val bookName: String = "...",
    val chapterNumber: Int = 0
) {
    val displayText: String
        get() = if (bookName != "..." && chapterNumber > 0) "$bookName $chapterNumber" else "Loading..."
}
