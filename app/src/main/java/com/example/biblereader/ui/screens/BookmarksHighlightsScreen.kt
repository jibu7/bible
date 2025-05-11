package com.example.biblereader.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete // Import Delete icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.data.model.BookmarkWithContext
import com.example.biblereader.data.model.HighlightWithContext
import com.example.biblereader.ui.viewmodels.BookmarksHighlightsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookmarksHighlightsScreen(
    navController: NavController,
    viewModel: BookmarksHighlightsViewModel = hiltViewModel()
) {
    val bookmarks by viewModel.bookmarks.collectAsState()
    val highlights by viewModel.highlights.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 }) // 0 for Bookmarks, 1 for Highlights
    val coroutineScope = rememberCoroutineScope()
    val tabs = listOf("Bookmarks", "Highlights")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks & Highlights") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> UserDataList(
                        items = bookmarks,
                        contentType = "Bookmark",
                        itemContent = { bookmark ->
                            UserItemContent(
                                reference = bookmark.reference,
                                text = bookmark.translatedText
                            )
                        },
                        onItemClick = { bookmark ->
                            navController.navigate("verses/${bookmark.chapterId}")
                            // TODO: Scroll to verse bookmark.verseId
                        },
                        onItemDelete = { bookmark ->
                            viewModel.removeBookmarkByVerseId(bookmark.verseId) // Using verseId for deletion
                        }
                    )
                    1 -> UserDataList(
                        items = highlights,
                        contentType = "Highlight",
                        itemContent = { highlight ->
                            UserItemContent(
                                reference = highlight.reference,
                                text = highlight.translatedText,
                                // Optionally show highlight color indicator
                                modifier = Modifier // Example: .background(Color(android.graphics.Color.parseColor(highlight.highlightColorHex ?: "#FFFFFFFF")))
                            )
                        },
                        onItemClick = { highlight ->
                             navController.navigate("verses/${highlight.chapterId}")
                             // TODO: Scroll to verse highlight.verseId
                        },
                        onItemDelete = { highlight ->
                            viewModel.removeHighlightByVerseId(highlight.verseId) // Using verseId for deletion
                        }
                    )
                }
            }
        }
    }
}

// Generic list composable
@Composable
fun <T> UserDataList(
    items: List<T>,
    contentType: String,
    itemContent: @Composable (T) -> Unit,
    onItemClick: (T) -> Unit,
    onItemDelete: (T) -> Unit
) {
    if (items.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No ${contentType}s saved yet.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(items, key = { item -> item.hashCode() }) { item -> // Use hashCode or specific ID if available
                UserDataItem(
                    item = item,
                    onClick = { onItemClick(item) },
                    onDelete = { onItemDelete(item) },
                    content = { itemContent(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

// Generic item composable with delete action
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> UserDataItem(
    item: T,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    ListItem(
        headlineContent = { content() },
        modifier = Modifier.clickable(onClick = onClick),
        trailingContent = {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}

// Content specific composable (reference + text snippet)
@Composable
fun UserItemContent(
    reference: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(reference, style = MaterialTheme.typography.titleMedium)
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
