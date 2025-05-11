package com.example.biblereader.ui.viewmodels

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.BookmarkEntity
import com.example.biblereader.data.local.db.entities.HighlightEntity
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ExportImportViewModel @Inject constructor(
    private val repository: AppRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _exportStatus = MutableStateFlow<String>("")
    val exportStatus: StateFlow<String> = _exportStatus

    private val _importStatus = MutableStateFlow<String>("")
    val importStatus: StateFlow<String> = _importStatus

    fun exportUserData() {
        viewModelScope.launch {
            try {
                val bookmarks = repository.exportBookmarks()
                val highlights = repository.exportHighlights()

                val jsonObject = JSONObject().apply {
                    put("bookmarks", JSONArray().apply {
                        bookmarks.forEach { bookmark ->
                            put(JSONObject().apply {
                                put("id", bookmark.id)
                                put("verseId", bookmark.verseId)
                                put("timestamp", bookmark.timestamp)
                            })
                        }
                    })
                    put("highlights", JSONArray().apply {
                        highlights.forEach { highlight ->
                            put(JSONObject().apply {
                                put("id", highlight.id)
                                put("verseId", highlight.verseId)
                                put("colorHex", highlight.colorHex ?: "")
                                put("timestamp", highlight.timestamp)
                            })
                        }
                    })
                }

                val fileName = "bible_app_data_${System.currentTimeMillis()}.json"
                val file = File(context.getExternalFilesDir(null), fileName)
                file.writeText(jsonObject.toString(2))

                _exportStatus.value = "Data exported successfully to: ${file.absolutePath}"
            } catch (e: Exception) {
                _exportStatus.value = "Export failed: ${e.message}"
            }
        }
    }

    fun importUserData(uri: Uri) {
        viewModelScope.launch {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                if (inputStream == null) {
                    _importStatus.value = "Failed to open file"
                    return@launch
                }

                val jsonString = inputStream.bufferedReader().use { it.readText() }
                inputStream.close()

                val jsonObject = JSONObject(jsonString)

                val bookmarks = mutableListOf<BookmarkEntity>()
                val highlights = mutableListOf<HighlightEntity>()

                // Parse bookmarks
                jsonObject.optJSONArray("bookmarks")?.let { bookmarksArray ->
                    for (i in 0 until bookmarksArray.length()) {
                        val bookmarkObj = bookmarksArray.getJSONObject(i)
                        bookmarks.add(
                            BookmarkEntity(
                                id = bookmarkObj.getInt("id"),
                                verseId = bookmarkObj.getInt("verseId"),
                                timestamp = bookmarkObj.getLong("timestamp")
                            )
                        )
                    }
                }

                // Parse highlights
                jsonObject.optJSONArray("highlights")?.let { highlightsArray ->
                    for (i in 0 until highlightsArray.length()) {
                        val highlightObj = highlightsArray.getJSONObject(i)
                        val colorHex = highlightObj.optString("colorHex", "").takeIf { it.isNotEmpty() }
                        highlights.add(
                            HighlightEntity(
                                id = highlightObj.getInt("id"),
                                verseId = highlightObj.getInt("verseId"),
                                colorHex = colorHex,
                                timestamp = highlightObj.getLong("timestamp")
                            )
                        )
                    }
                }

                // Import the data
                repository.importBookmarks(bookmarks)
                repository.importHighlights(highlights)

                _importStatus.value = "Data imported successfully"
            } catch (e: Exception) {
                _importStatus.value = "Import failed: ${e.message}"
            }
        }
    }
} 