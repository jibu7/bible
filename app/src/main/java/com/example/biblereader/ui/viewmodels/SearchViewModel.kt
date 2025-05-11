package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.data.preferences.UserPreferencesRepository
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.example.biblereader.data.model.VerseSearchResult

// Data class to hold combined search results
data class SearchResults(
    val books: List<BookEntity> = emptyList(),
    val verses: List<VerseSearchResult> = emptyList()
    // Add verse reference results later
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class) // For debounce and flatMapLatest
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: AppRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val languageId: StateFlow<Int> = userPreferencesRepository.selectedLanguageId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1 // Default language ID - Ensure this matches UserPreferencesRepository
        )

    // Debounce the query to avoid searching on every keystroke
    private val debouncedQuery = _searchQuery
        .debounce(300) // Wait for 300ms of silence before triggering search
        .distinctUntilChanged() // Only search if query text actually changed

    // Flow that combines results from book and verse searches
    val searchResults: StateFlow<SearchResults> = combine(
        debouncedQuery,
        languageId
    ) { query, langId -> Pair(query, langId) }
    .flatMapLatest { (query, langId) ->
        if (query.isBlank() || query.length < 2) { // Basic validation: don't search for empty/very short strings
            flowOf(SearchResults()) // Return empty results
        } else {
            // Combine flows for book and verse searches
            combine(
                repository.searchBooks(query),
                repository.searchVersesByKeyword(query, langId)
            ) { booksResult: List<BookEntity>, versesResult: List<VerseSearchResult> ->
                SearchResults(books = booksResult, verses = versesResult)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchResults() // Initial empty state
    )

    // Function called by the UI when the search text changes
    fun onQueryChange(query: String) {
        _searchQuery.value = query
    }

    // TODO: Add function to parse verse reference query and call repository.searchByVerseReference
}
