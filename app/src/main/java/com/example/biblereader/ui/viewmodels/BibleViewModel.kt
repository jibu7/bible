package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.data.local.db.entities.ChapterEntity
import com.example.biblereader.data.local.db.entities.VerseEntity
import com.example.biblereader.data.model.VerseSearchResult
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class BibleViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    // Expose all books as a Flow
    val books: Flow<List<BookEntity>> = repository.getAllBooks()

    // Get a specific book by ID
    fun getBook(bookId: Int): Flow<BookEntity?> {
        return repository.getAllBooks().map { books ->
            books.find { it.bookId == bookId }
        }
    }

    // Get a specific chapter by book ID and chapter number
    fun getChapter(bookId: Int, chapterNumber: Int): Flow<ChapterEntity?> {
        return repository.getChaptersByBook(bookId).map { chapters ->
            chapters.find { it.chapterNumber == chapterNumber }
        }
    }

    // Get all chapters for a book
    fun getChapters(bookId: Int): Flow<List<ChapterEntity>> {
        return repository.getChaptersByBook(bookId)
    }

    // Get all verses for a chapter
    fun getVerses(chapterId: Int): Flow<List<VerseEntity>> {
        return repository.getVersesByChapter(chapterId)
    }

    // Search verses by reference (e.g., "John 3:16")
    fun searchVerses(query: String): Flow<List<VerseSearchResult>> {
        // For simplicity, we'll use the keyword search with the default language (1)
        // In a real implementation, you might want to parse the reference and use searchByVerseReference
        return repository.searchVersesByKeyword(query, 1)
    }
} 