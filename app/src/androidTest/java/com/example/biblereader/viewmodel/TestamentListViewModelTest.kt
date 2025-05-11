package com.example.biblereader.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.data.Book
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestamentListViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    private lateinit var viewModel: TestamentListViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        
        // Create mock repository
        val mockRepository = mockk<AppRepository>()
        
        // Create mock data for Old Testament books
        val oldTestamentBooks = listOf(
            BookEntity(bookId = 1, name = "Genesis", testament = "Old Testament", bookOrder = 1),
            BookEntity(bookId = 39, name = "Malachi", testament = "Old Testament", bookOrder = 39)
        )
        
        // Create mock data for New Testament books
        val newTestamentBooks = listOf(
            BookEntity(bookId = 40, name = "Matthew", testament = "New Testament", bookOrder = 40),
            BookEntity(bookId = 66, name = "Revelation", testament = "New Testament", bookOrder = 66)
        )
        
        // Set up mock responses
        every { mockRepository.getBooksByTestament("Old Testament") } returns flowOf(oldTestamentBooks)
        every { mockRepository.getBooksByTestament("New Testament") } returns flowOf(newTestamentBooks)
        
        // Create viewModel with mock repository
        viewModel = TestamentListViewModel(mockRepository)
    }

    @Test
    fun getOldTestamentBooks_returnsCorrectBooks() = runBlocking {
        // When
        val books: List<Book> = viewModel.getOldTestamentBooks().first()

        // Then
        assert(books.isNotEmpty())
        assert(books.first().name == "Genesis")
        assert(books.last().name == "Malachi")
        assert(books.size == 2) // We only have 2 mock books
    }

    @Test
    fun getNewTestamentBooks_returnsCorrectBooks() = runBlocking {
        // When
        val books: List<Book> = viewModel.getNewTestamentBooks().first()

        // Then
        assert(books.isNotEmpty())
        assert(books.first().name == "Matthew")
        assert(books.last().name == "Revelation")
        assert(books.size == 2) // We only have 2 mock books
    }
} 