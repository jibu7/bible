package com.example.biblereader.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.biblereader.ui.animation.AnimatedVerseCard
import com.example.biblereader.ui.animation.AnimatedBookmarkIcon
import com.example.biblereader.ui.animation.AnimatedSearchBar
import com.example.biblereader.ui.animation.AnimatedHighlight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibleVerseCard(
    verseText: String,
    verseNumber: Int,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
    onVerseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVerseCard(visible = true) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = verseNumber.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onBookmarkClick) {
                        AnimatedBookmarkIcon(isBookmarked = isBookmarked) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
                Text(
                    text = verseText,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedSearchBar(visible = true) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("Search verses...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { onQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HighlightedText(
    text: String,
    isHighlighted: Boolean,
    onHighlightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedHighlight(visible = isHighlighted) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(4.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun LoadingIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isLoading,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Retry")
        }
    }
} 