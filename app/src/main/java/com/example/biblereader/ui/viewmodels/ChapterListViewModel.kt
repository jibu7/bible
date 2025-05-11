package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.data.local.db.entities.ChapterEntity
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class) // For flatMapLatest used on StateFlow
@HiltViewModel
class ChapterListViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Get bookId from navigation arguments
    private val bookId: StateFlow<Int> = savedStateHandle.getStateFlow("bookId", -1)

    // Flow for the current BookEntity
    private val _currentBook = MutableStateFlow<BookEntity?>(null)
    val currentBook: StateFlow<BookEntity?> = _currentBook.asStateFlow()

    // Flow for the list of chapters
    val chapters: StateFlow<List<ChapterEntity>> = bookId.flatMapLatest { id ->
        if (id != -1) {
            // Fetch book details when bookId is available
            fetchBookDetails(id)
            repository.getChaptersByBook(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Helper function to fetch book details separately
    private fun fetchBookDetails(id: Int) {
        viewModelScope.launch {
            _currentBook.value = repository.getBookById(id)
        }
    }

    // Define a default/placeholder language ID.
    // This should ideally come from user preferences/settings later.
    // Ensure '1' corresponds to a valid language ID in your LanguageEntity table (e.g., English).
    val defaultLanguageId = 1
}
