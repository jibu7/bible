package com.example.biblereader.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.biblereader.data.local.db.entities.LanguageEntity
import com.example.biblereader.data.local.preferences.ThemePreferences
import com.example.biblereader.data.preferences.UserPreferencesRepository
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: AppRepository,
    private val themePreferences: ThemePreferences,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    // Flow of available languages from the database
    val availableLanguages: StateFlow<List<LanguageEntity>> = repository.getAllLanguages()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Flow of the currently selected language ID from DataStore
    val selectedLanguageId: StateFlow<Int> = userPreferencesRepository.selectedLanguageId
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 1 // Default to first language
        )

    val isDarkModeForced: StateFlow<Boolean> = themePreferences.isDarkModeForced
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isDarkModeEnabled: StateFlow<Boolean> = themePreferences.isDarkModeEnabled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // Function to call when the user selects a new language
    fun onLanguageSelected(languageId: Int) {
        viewModelScope.launch {
            userPreferencesRepository.updateSelectedLanguageId(languageId)
        }
    }

    fun setDarkModeForced(isForced: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkModeForced(isForced)
        }
    }

    fun setDarkModeEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkModeEnabled(isEnabled)
        }
    }
}
