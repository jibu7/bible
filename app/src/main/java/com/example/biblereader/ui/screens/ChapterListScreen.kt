package com.example.biblereader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.data.local.db.entities.ChapterEntity
import com.example.biblereader.ui.viewmodels.ChapterListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterListScreen(
    navController: NavController,
    viewModel: ChapterListViewModel = hiltViewModel()
) {
    val chapters by viewModel.chapters.collectAsState()
    val book by viewModel.currentBook.collectAsState()
    val languageId = viewModel.defaultLanguageId // Use the default for now

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = book?.name ?: "Chapters") }, // Show book name or default
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (chapters.isEmpty()) {
             Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                 // CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                 Text("Loading chapters...", modifier = Modifier.padding(16.dp))
             }
        } else {
            // Use a grid for chapter numbers
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 80.dp), // Adjust minSize as needed
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chapters) { chapter ->
                    ChapterItem(chapter = chapter) {
                        // Navigate to VerseViewScreen, passing chapter ID
                        navController.navigate("verses/${chapter.chapterId}") // Use chapterId
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterItem(
    chapter: ChapterEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            // .aspectRatio(1f) // Make items squareish if desired
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().padding(8.dp), // Fill card space
            contentAlignment = Alignment.Center // Center the number
        ) {
            Text(
                text = chapter.chapterNumber.toString(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}
