package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookListViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle // Hilt provides this automatically
) : ViewModel() {

    // Get testament from navigation arguments (key must match the route definition)
    private val testament: StateFlow<String> = savedStateHandle.getStateFlow("testament", "Unknown")

    // Flow representing the list of books for the current testament
    val books: StateFlow<List<BookEntity>> = testament.flatMapLatest { name ->
        // Map "Old Testament" / "New Testament" to the strings used in your BookEntity
        // Based on the books.csv file, the testament values are:
        // "Isezerano rya Kera" for Old Testament
        // "Isezerano Rishya" for New Testament
        val testamentQuery = when (name) {
            "Old" -> "Isezerano rya Kera"
            "New" -> "Isezerano Rishya"
            else -> "" // Handle unknown/default case ("Unknown" from getStateFlow)
        }
        if (testamentQuery.isNotEmpty()) {
            repository.getBooksByTestament(testamentQuery)
        } else {
            flowOf(emptyList()) // Return empty list if testament name is invalid
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Keep flow active for 5s after last subscriber gone
        initialValue = emptyList() // Initial value while loading
    )

     // Expose testament name for the UI (e.g., AppBar title)
    val currentTestamentName: StateFlow<String> = testament
}
