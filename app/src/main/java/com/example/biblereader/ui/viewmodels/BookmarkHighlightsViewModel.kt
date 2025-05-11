package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.model.BookmarkWithContext
import com.example.biblereader.data.model.HighlightWithContext
import com.example.biblereader.data.preferences.UserPreferencesRepository
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BookmarksHighlightsViewModel @Inject constructor(
    private val repository: AppRepository,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val languageId: StateFlow<Int> = userPreferencesRepository.selectedLanguageId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1 // Default language ID
        )

    // Fetch bookmarks with context based on selected language
    val bookmarks: StateFlow<List<BookmarkWithContext>> = languageId.flatMapLatest { langId ->
        repository.getAllBookmarksWithContext(langId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Fetch highlights with context based on selected language
    val highlights: StateFlow<List<HighlightWithContext>> = languageId.flatMapLatest { langId ->
        repository.getAllHighlightsWithContext(langId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Functions to remove items
    fun removeBookmark(bookmarkId: Int) {
        // Note: We don't have the BookmarkEntity easily available here anymore.
        // Need to adjust the DAO/Repository remove methods if they expect the entity.
        // Option 1: DAO `deleteBookmarkById(id: Int)`
        // Option 2: Pass verseId if DAO uses `deleteBookmarkByVerseId(verseId: Int)`
        // Let's assume we add `deleteBookmarkById` to the DAO/Repo.
        viewModelScope.launch {
            // TODO: Update BookmarkDao and AppRepository with deleteBookmarkById(bookmarkId: Int)
            // repository.removeBookmarkById(bookmarkId)
            println("TODO: Implement removeBookmarkById in Repo/DAO using ID: $bookmarkId")
        }
    }

    fun removeHighlight(highlightId: Int) {
        viewModelScope.launch {
            // TODO: Update HighlightDao and AppRepository with deleteHighlightById(highlightId: Int)
            // repository.removeHighlightById(highlightId)
             println("TODO: Implement removeHighlightById in Repo/DAO using ID: $highlightId")
        }
    }
     // OR if using verseId for deletion:
     fun removeBookmarkByVerseId(verseId: Int) {
         viewModelScope.launch {
             repository.removeBookmark(verseId) // Assumes removeBookmark uses verseId
         }
     }
     fun removeHighlightByVerseId(verseId: Int) {
         viewModelScope.launch {
             repository.removeHighlight(verseId) // Assumes removeHighlight uses verseId
         }
     }
}
