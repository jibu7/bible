package com.example.biblereader.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Use autoMirrored for RTL support
import androidx.compose.material.icons.filled.Search // Import search icon
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.biblereader.data.local.db.entities.BookEntity
import com.example.biblereader.ui.viewmodels.BookListViewModel

@OptIn(ExperimentalMaterial3Api::class) // For TopAppBar
@Composable
fun BookListScreen(
    navController: NavController,
    viewModel: BookListViewModel = hiltViewModel() // Let Hilt provide the ViewModel
) {
    // No need to pass testamentName manually, ViewModel gets it from SavedStateHandle
    val books by viewModel.books.collectAsState()
    val testamentName by viewModel.currentTestamentName.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = testamentName) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Go back
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = { // Add actions block
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (books.isEmpty()) {
             // Show a loading indicator or empty state message
             Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                 // CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                 Text("Loading books or no books found...", modifier = Modifier.padding(16.dp))
             }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(books) { book ->
                    BookItem(book = book) {
                        // Navigate to ChapterListScreen, passing the book ID
                        // Make sure bookId is not null before navigating
                        book.bookId?.let { id ->
                            navController.navigate("chapters/$id")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookItem(
    book: BookEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = book.name, // Display the full book name
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp) // Adjust padding as needed
        )
        // Optional: Display abbreviation or other info
        /*
        Text(
            text = "(${book.abbreviation})",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
        */
    }
}
