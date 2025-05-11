package com.example.biblereader.viewmodel

import com.example.biblereader.data.Book
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ViewModel for displaying and managing the list of testaments and their books.
 */
class TestamentListViewModel @Inject constructor(
    private val repository: AppRepository
) {
    /**
     * Get all books from the Old Testament.
     * @return A Flow of Book objects representing Old Testament books.
     */
    fun getOldTestamentBooks(): Flow<List<Book>> {
        return repository.getBooksByTestament("Old Testament")
            .map { bookEntities -> bookEntities.map { it.toBook() } }
    }

    /**
     * Get all books from the New Testament.
     * @return A Flow of Book objects representing New Testament books.
     */
    fun getNewTestamentBooks(): Flow<List<Book>> {
        return repository.getBooksByTestament("New Testament")
            .map { bookEntities -> bookEntities.map { it.toBook() } }
    }

    /**
     * Extension function to convert a BookEntity to a Book.
     */
    private fun BookEntity.toBook(): Book {
        return Book(
            id = bookId ?: 0,
            name = name,
            testament = testament,
            abbreviation = name.substring(0, minOf(3, name.length)).uppercase(),
            bookOrder = bookOrder
        )
    }
} 