package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.BookmarkEntity
import com.example.biblereader.data.local.db.entities.HeadingEntity
import com.example.biblereader.data.local.db.entities.TranslationEntity
import com.example.biblereader.data.local.db.entities.VerseEntity
import com.example.biblereader.data.local.db.entities.HighlightEntity
import com.example.biblereader.repository.AppRepository
import com.example.biblereader.ui.model.ChapterReference
import com.example.biblereader.ui.model.DisplayItem
import com.example.biblereader.data.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class VerseViewModel @Inject constructor(
    private val repository: AppRepository,
    savedStateHandle: SavedStateHandle,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val chapterId: StateFlow<Int> = savedStateHandle.getStateFlow("chapterId", -1)
    private val languageId: StateFlow<Int> = userPreferencesRepository.selectedLanguageId
         .stateIn(
             scope = viewModelScope,
             started = SharingStarted.WhileSubscribed(5000),
             initialValue = -1
         )

    // Flow for chapter details (Book Name + Chapter Number)
    val chapterReference: StateFlow<ChapterReference> = chapterId.filter { it != -1 }.flatMapLatest { id ->
        flow {
            val chapter = repository.getChapterById(id)
            if (chapter != null) {
                val book = repository.getBookById(chapter.bookId)
                emit(ChapterReference(book?.name ?: "Book", chapter.chapterNumber))
            } else {
                emit(ChapterReference()) // Emit default if not found
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ChapterReference() // Initial loading state
    )

    // Define flows for bookmark and highlight status for the current chapter's verses
    // These will refetch when chapterId changes
    private val bookmarksFlow: StateFlow<Set<Int>> = chapterId.filter { it != -1 }.flatMapLatest { chapId ->
        // Get all verses for the chapter, then check bookmark status for each
        // This is less efficient than a dedicated DAO query, but simpler for now
        // Alternative: DAO method `getBookmarkedVerseIdsForChapter(chapterId)`
        repository.getVersesByChapter(chapId).map { verses: List<VerseEntity> -> // Explicitly type 'verses'
             verses.map { it.verseId }.toSet() // Use verseId
        }.flatMapLatest { verseIds ->
             if (verseIds.isEmpty()) flowOf(emptySet<Int>()) else repository.getAllBookmarks()
                .map { bookmarks: List<BookmarkEntity> -> // Explicit type for bookmarks
                    bookmarks.filter { it.verseId in verseIds }.map { it.verseId }.toSet<Int>() // Use verseId
                }
         }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet<Int>())


    private val highlightsFlow: StateFlow<Map<Int, HighlightEntity?>> = chapterId.filter { it != -1 }.flatMapLatest { chapId ->
         repository.getVersesByChapter(chapId).map { verses: List<VerseEntity> -> // Explicit type for verses
             verses.map { it.verseId }.toSet<Int>() // Use verseId
         }.flatMapLatest { verseIds: Set<Int> -> // Explicit type for verseIds
             if (verseIds.isEmpty()) flowOf(emptyMap<Int, HighlightEntity?>()) else repository.getAllHighlights()
                 .map { highlights: List<HighlightEntity> -> // Explicit type for highlights
                     highlights.filter { it.verseId in verseIds }.associateBy { it.verseId } // Use verseId
                 }
         }
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap<Int, HighlightEntity?>())


    // Combined flow for verses, translations, headings, bookmarks, highlights
    val displayItems: StateFlow<List<DisplayItem>> = combine(
        chapterId,
        languageId,
        bookmarksFlow, // Add bookmark status flow
        highlightsFlow // Add highlight status flow
    ) { chapId, langId, bookmarkedIds, highlightedMap ->
        Triple(chapId, langId, Pair(bookmarkedIds, highlightedMap)) // Combine IDs and status maps
    }
    .filter { it.first != -1 && it.second != -1 } // Ensure IDs are valid
    .flatMapLatest { (chapId, langId, statuses) ->
        val bookmarkedIds = statuses.first
        val highlightedMap = statuses.second
        // Combine the data flows needed for the display list
        combine<List<VerseEntity>, List<TranslationEntity>, List<HeadingEntity>, List<DisplayItem>>( // Explicit types for combine
            repository.getVersesByChapter(chapId),
            repository.getTranslationsForChapter(chapId, langId),
            repository.getHeadingsByChapter(chapId)
        ) { verses, translations, headings ->
            // Process and merge the lists, now including status
            createDisplayList(verses, translations, headings, bookmarkedIds, highlightedMap)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList<DisplayItem>() // Specify type argument
    )

    // Merging logic (can be complex depending on heading rules)
    private fun createDisplayList(
        verses: List<VerseEntity>,
        translations: List<TranslationEntity>,
        headings: List<HeadingEntity>,
        bookmarkedVerseIds: Set<Int>, // Add bookmark status
        highlightedVerseMap: Map<Int, HighlightEntity?> // Add highlight status
    ): List<DisplayItem> {

        if (verses.isEmpty() || translations.isEmpty()) {
            // If no verses or translations, return empty or a placeholder
            return emptyList()
            // Alternatively: return listOf(DisplayItem.HeadingDisplay(-1, "No content available"))
        }

        // Use explicit lambda parameter names and types for clarity
        val translationMap = translations.associateBy(
            keySelector = { translation: TranslationEntity -> translation.verseId },
            valueTransform = { translation: TranslationEntity -> translation.verseText }
        )
        // Group headings by the verse number they should appear BEFORE.
        // Assumption: headingOrder now serves the purpose of the old startVerseNumber.
        val headingsMap = headings.groupBy { it.headingOrder }

        val combinedList = mutableListOf<DisplayItem>()

        // Ensure verses are sorted by verse number for correct ordering
        val sortedVerses = verses.sortedBy { it.verseNumber }

        sortedVerses.forEach { verse ->
            // Add headings that should appear *before* this verse number.
            // Look up using verse.verseNumber, assuming it corresponds to headingOrder.
            headingsMap[verse.verseNumber]?.sortedBy { it.headingOrder }?.forEach { heading ->
                combinedList.add(DisplayItem.HeadingDisplay(heading.headingId, heading.headingText)) // Use headingId
            }

            // Add the verse itself
            val verseText = translationMap[verse.verseId] ?: "[Translation missing]" // Use verseId
            val isBookmarked = bookmarkedVerseIds.contains(verse.verseId) // Use verseId
            val highlightInfo = highlightedVerseMap[verse.verseId] // Use verseId
            val isHighlighted = highlightInfo != null
            val highlightColor = highlightInfo?.colorHex // Get color if available

            combinedList.add(
                DisplayItem.VerseDisplay(
                    verseId = verse.verseId, // Use verseId
                    verseNumber = verse.verseNumber,
                    text = verseText,
                    isBookmarked = isBookmarked, // Pass status
                    isHighlighted = isHighlighted, // Pass status
                    highlightColor = highlightColor // Pass color
                )
            )
        }
        return combinedList
    }

    // --- Actions ---
    fun toggleBookmark(verseId: Int) {
        viewModelScope.launch {
            // Check current status (can be slightly delayed, consider Flow.first() for immediate check if needed)
            val isCurrentlyBookmarked = bookmarksFlow.value.contains(verseId)
            if (isCurrentlyBookmarked) {
                repository.removeBookmark(verseId)
            } else {
                repository.addBookmark(verseId)
            }
        }
    }

    fun toggleHighlight(verseId: Int) {
         // TODO: Implement color selection later if needed. For now, just toggle on/off.
        viewModelScope.launch {
            val isCurrentlyHighlighted = highlightsFlow.value.containsKey(verseId)
             if (isCurrentlyHighlighted) {
                 repository.removeHighlight(verseId)
             } else {
                 repository.addHighlight(verseId) // Add default highlight
             }
        }
    }
}
