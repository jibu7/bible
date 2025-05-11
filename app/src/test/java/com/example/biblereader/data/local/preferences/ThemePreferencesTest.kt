package com.example.biblereader.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ThemePreferencesTest {
    @get:Rule
    val tempFolder = TemporaryFolder()

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var themePreferences: ThemePreferences
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val isDarkModeForcedKey = booleanPreferencesKey("is_dark_mode_forced")
    private val isDarkModeEnabledKey = booleanPreferencesKey("is_dark_mode_enabled")

    @Before
    fun setup() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { tempFolder.newFile("test.preferences_pb") }
        )
        themePreferences = ThemePreferences(dataStore)
    }

    @After
    fun tearDown() {
        // No need for explicit cleanup as TestScope handles it automatically
    }

    @Test
    fun `isDarkModeForced returns false when not set`() = testScope.runTest {
        // Act
        val result = themePreferences.isDarkModeForced.first()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isDarkModeForced returns true when set to true`() = testScope.runTest {
        // Arrange
        dataStore.edit { preferences ->
            preferences[isDarkModeForcedKey] = true
        }

        // Act
        val result = themePreferences.isDarkModeForced.first()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isDarkModeEnabled returns false when not set`() = testScope.runTest {
        // Act
        val result = themePreferences.isDarkModeEnabled.first()

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isDarkModeEnabled returns true when set to true`() = testScope.runTest {
        // Arrange
        dataStore.edit { preferences ->
            preferences[isDarkModeEnabledKey] = true
        }

        // Act
        val result = themePreferences.isDarkModeEnabled.first()

        // Assert
        assertTrue(result)
    }

    @Test
    fun `setDarkModeForced updates the stored value`() = testScope.runTest {
        // Act
        themePreferences.setDarkModeForced(true)

        // Assert
        val result = dataStore.data.first()
        assertTrue(result[isDarkModeForcedKey] ?: false)
    }

    @Test
    fun `setDarkModeEnabled updates the stored value`() = testScope.runTest {
        // Act
        themePreferences.setDarkModeEnabled(true)

        // Assert
        val result = dataStore.data.first()
        assertTrue(result[isDarkModeEnabledKey] ?: false)
    }
} 