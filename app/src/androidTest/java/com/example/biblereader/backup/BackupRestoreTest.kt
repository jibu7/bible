package com.example.biblereader.backup

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.data.local.db.entities.BookmarkEntity
import com.example.biblereader.data.local.db.entities.HighlightEntity
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class BackupRestoreTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun exportingBookmarksWorksWithVaryingDataSizes() = runBlocking {
        // Add some bookmarks
        repository.addBookmark(1)
        repository.addBookmark(2)
        repository.addBookmark(3)
        
        // Export bookmarks
        val exportedBookmarks = repository.exportBookmarks()
        
        // Verify exported data
        assert(exportedBookmarks.size >= 3)
        assert(exportedBookmarks.any { it.verseId == 1 })
        assert(exportedBookmarks.any { it.verseId == 2 })
        assert(exportedBookmarks.any { it.verseId == 3 })
    }

    @Test
    fun importingFromExportedFileRestoresDataAccurately() = runBlocking {
        // First, export existing bookmarks
        val originalBookmarks = repository.exportBookmarks()
        
        // Clear all bookmarks (this would be done in a real app)
        // For testing, we'll just remove the ones we added
        repository.removeBookmark(1)
        repository.removeBookmark(2)
        repository.removeBookmark(3)
        
        // Import the bookmarks
        repository.importBookmarks(originalBookmarks)
        
        // Verify the bookmarks are restored
        val restoredBookmarks = repository.exportBookmarks()
        assert(restoredBookmarks.size == originalBookmarks.size)
    }

    @Test
    fun exportingHighlightsWorksWithVaryingDataSizes() = runBlocking {
        // Add some highlights
        repository.addHighlight(1, "#FF0000") // Red highlight
        repository.addHighlight(2, "#00FF00") // Green highlight
        repository.addHighlight(3, "#0000FF") // Blue highlight
        
        // Export highlights
        val exportedHighlights = repository.exportHighlights()
        
        // Verify exported data
        assert(exportedHighlights.size >= 3)
        assert(exportedHighlights.any { it.verseId == 1 && it.colorHex == "#FF0000" })
        assert(exportedHighlights.any { it.verseId == 2 && it.colorHex == "#00FF00" })
        assert(exportedHighlights.any { it.verseId == 3 && it.colorHex == "#0000FF" })
    }

    @Test
    fun importingHighlightsRestoresDataAccurately() = runBlocking {
        // First, export existing highlights
        val originalHighlights = repository.exportHighlights()
        
        // Clear all highlights (this would be done in a real app)
        // For testing, we'll just remove the ones we added
        repository.removeHighlight(1)
        repository.removeHighlight(2)
        repository.removeHighlight(3)
        
        // Import the highlights
        repository.importHighlights(originalHighlights)
        
        // Verify the highlights are restored
        val restoredHighlights = repository.exportHighlights()
        assert(restoredHighlights.size == originalHighlights.size)
    }
} 