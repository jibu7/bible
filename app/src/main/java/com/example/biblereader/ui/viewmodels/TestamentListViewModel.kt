package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// Define a simple data class or use Strings directly
data class TestamentDisplay(val name: String) // e.g., "Isezerano rya Kera"

@HiltViewModel
class TestamentListViewModel @Inject constructor(
    // Inject AppRepository if needed for future dynamic logic
    // private val repository: AppRepository
) : ViewModel() {

    // For now, use a static list. Could fetch from repository if needed.
    private val _testaments = MutableStateFlow<List<TestamentDisplay>>(
        listOf(
            TestamentDisplay("Isezerano rya Kera"),
            TestamentDisplay("Isezerano Rishya")
        )
    )
    val testaments: StateFlow<List<TestamentDisplay>> = _testaments

    // Add any event handling functions if needed later
}
