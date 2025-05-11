package com.example.biblereader.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Define the DataStore instance at the top level
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val selectedLanguageIdKey = intPreferencesKey("selected_language_id")

    // Default language ID (e.g., English). Ensure this ID exists in your LanguageEntity table.
    // TODO: Consider fetching the ID for a default language code ("en") on first launch if needed.
    private val DEFAULT_LANGUAGE_ID = 1

    // Flow to observe the selected language ID
    val selectedLanguageId: Flow<Int> = dataStore.data
        .map { preferences -> preferences[selectedLanguageIdKey] ?: DEFAULT_LANGUAGE_ID }

    // Function to update the selected language ID
    suspend fun updateSelectedLanguageId(languageId: Int) {
        dataStore.edit { preferences ->
            preferences[selectedLanguageIdKey] = languageId
        }
    }

    companion object {
        fun createForTesting(dataStore: DataStore<Preferences>): UserPreferencesRepository {
            return UserPreferencesRepository(dataStore)
        }
    }
}
