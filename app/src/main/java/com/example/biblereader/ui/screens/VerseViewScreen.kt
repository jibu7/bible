package com.example.biblereader.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.ui.model.DisplayItem
import com.example.biblereader.ui.viewmodels.VerseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerseViewScreen(
    navController: NavController,
    viewModel: VerseViewModel = hiltViewModel()
) {
    val displayItems by viewModel.displayItems.collectAsState()
    val chapterRef by viewModel.chapterReference.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = chapterRef.displayText) }, // Display "Book Chapter"
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (displayItems.isEmpty() && chapterRef.bookName == "...") {
            // Initial loading state
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                 CircularProgressIndicator()
            }
        } else if (displayItems.isEmpty()) {
            // Empty state after loading
             Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                 Text("No verses found for this chapter.", modifier = Modifier.padding(16.dp))
             }
        }
         else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(
                    items = displayItems,
                    key = { item -> // Provide stable keys for performance and animations
                        when (item) {
                            is DisplayItem.HeadingDisplay -> "h_${item.headingId}"
                            is DisplayItem.VerseDisplay -> "v_${item.verseId}"
                        }
                    }
                ) { item ->
                    when (item) {
                        is DisplayItem.HeadingDisplay -> HeadingItem(item)
                        is DisplayItem.VerseDisplay -> VerseItem(
                            item = item,
                            onBookmarkClick = { viewModel.toggleBookmark(item.verseId) },
                            onHighlightClick = { viewModel.toggleHighlight(item.verseId) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HeadingItem(item: DisplayItem.HeadingDisplay) {
    Text(
        text = item.text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp) // Add spacing around headings
    )
}

@Composable
fun VerseItem(
    item: DisplayItem.VerseDisplay,
    onBookmarkClick: () -> Unit,
    onHighlightClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)) {
            append("${item.verseNumber} ")
        }
        append(item.text)
    }

    // Determine background color based on highlight status
    val highlightColor = if (item.isHighlighted) {
        // TODO: Use item.highlightColor if implementing color choice
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) // Example: light highlight
    } else {
        Color.Transparent // Default background
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(highlightColor) // Apply highlight background
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top // Align icons to top
    ) {
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f) // Text takes available space
        )

        // Add action icons
        IconButton(onClick = onBookmarkClick, modifier = Modifier.size(36.dp)) { // Smaller touch target
            Icon(
                imageVector = if (item.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = if (item.isBookmarked) "Remove Bookmark" else "Add Bookmark",
                tint = if (item.isBookmarked) MaterialTheme.colorScheme.primary else LocalContentColor.current
            )
        }
        IconButton(onClick = onHighlightClick, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.Filled.FormatColorText, // Placeholder icon
                contentDescription = if (item.isHighlighted) "Remove Highlight" else "Add Highlight",
                tint = if (item.isHighlighted) MaterialTheme.colorScheme.primary else LocalContentColor.current
            )
        }
    }
}
