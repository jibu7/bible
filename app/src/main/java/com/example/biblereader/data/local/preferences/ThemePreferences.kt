package com.example.biblereader.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemePreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val isDarkModeForcedKey = booleanPreferencesKey("is_dark_mode_forced")
    private val isDarkModeEnabledKey = booleanPreferencesKey("is_dark_mode_enabled")

    val isDarkModeForced: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[isDarkModeForcedKey] ?: false }

    val isDarkModeEnabled: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[isDarkModeEnabledKey] ?: false }

    suspend fun setDarkModeForced(isForced: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkModeForcedKey] = isForced
        }
    }

    suspend fun setDarkModeEnabled(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[isDarkModeEnabledKey] = isEnabled
        }
    }
} 