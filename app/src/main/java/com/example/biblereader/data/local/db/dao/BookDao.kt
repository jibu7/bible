package com.example.biblereader.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.biblereader.data.local.db.entities.BookEntity
import kotlinx.coroutines.flow.Flow
import com.example.biblereader.data.model.BookmarkWithContext

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<BookEntity>)

    @Query("SELECT * FROM books ORDER BY bookId")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE testament = :testament ORDER BY bookId")
    fun getBooksByTestament(testament: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE name LIKE '%' || :query || '%' ORDER BY bookId")
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE bookId = :bookId")
    suspend fun getBookById(bookId: Int): BookEntity?

    // Add other necessary queries (e.g., get specific book by name)
    
    @Query("DELETE FROM books")
    suspend fun deleteAllBooks()
}
