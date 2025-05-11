package com.example.biblereader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search // Import Search icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.data.model.VerseSearchResult // Import the new data class
import com.example.biblereader.ui.viewmodels.SearchViewModel
import com.example.biblereader.ui.viewmodels.SearchResults // May need explicit import if not auto-resolved
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    // Request focus for the TextField when the screen enters composition
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        // Consider showing keyboard explicitly if needed: keyboardController?.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Search Input Field
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .focusRequester(focusRequester),
                label = { Text("Search Books or Verses") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true
            )

            // Results List
            if (query.isBlank() || query.length < 2) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Enter 2 or more characters to search.", fontStyle = FontStyle.Italic)
                }
            } else if (results.books.isEmpty() && results.verses.isEmpty()) {
                 Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results found for \"$query\".")
                 }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    // Book Results
                    if (results.books.isNotEmpty()) {
                        item {
                            Text(
                                "Books",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        items(results.books, key = { "book_${it.bookId}" }) { book -> // Use bookId for key
                            BookResultItem(book = book) {
                                // Navigate to ChapterList for this book
                                coroutineScope.launch { keyboardController?.hide() } // Hide keyboard before navigating
                                navController.navigate("chapters/${book.bookId}") // Use bookId for navigation
                            }
                        }
                    }

                    // Verse Results - UPDATE LIST TYPE
                    if (results.verses.isNotEmpty()) {
                        item {
                            Text(
                                "Verses",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .padding(top = if (results.books.isNotEmpty()) 16.dp else 0.dp)
                            )
                        }
                        items(results.verses, key = { "verse_${it.verseId}" }) { verseResult -> // Use verseResult
                            // Pass the VerseSearchResult directly
                            VerseResultItem(verseResult = verseResult) { vId, chapId ->
                                // Correct navigation using data from verseResult
                                coroutineScope.launch { keyboardController?.hide() }
                                navController.navigate("verses/${chapId}")
                                // TODO: Add logic to scroll/highlight verse vId in VerseViewScreen if desired
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Result Item Composables ---

@Composable
fun BookResultItem(
    book: BookEntity,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(book.name) },
        supportingContent = { Text(book.testament) }, // e.g., "Old Testament"
        modifier = Modifier.clickable(onClick = onClick)
    )
    HorizontalDivider()
}

@Composable
fun VerseResultItem(
    verseResult: VerseSearchResult, // Accept VerseSearchResult
    onClick: (verseId: Int, chapterId: Int) -> Unit
) {
    // Use the reference property and text from VerseSearchResult
    val contextText = verseResult.translatedText.take(100) + if (verseResult.translatedText.length > 100) "..." else ""

    ListItem(
        headlineContent = { Text(verseResult.reference) }, // Use formatted reference
        supportingContent = { Text(contextText) },
        modifier = Modifier.clickable {
            // Pass the correct IDs from verseResult
            onClick(verseResult.verseId, verseResult.chapterId)
        }
    )
    HorizontalDivider()
}
