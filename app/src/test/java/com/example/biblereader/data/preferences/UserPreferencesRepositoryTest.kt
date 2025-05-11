package com.example.biblereader.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.preferencesOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class UserPreferencesRepositoryTest {

    private lateinit var repository: UserPreferencesRepository
    private val dataStore: DataStore<Preferences> = mockk()

    @Before
    fun setup() {
        repository = UserPreferencesRepository.createForTesting(dataStore)
    }

    @Test
    fun `selectedLanguageId returns default value when not set`() = runTest {
        // Arrange
        val defaultLanguageId = 1
        every { dataStore.data } returns flowOf(preferencesOf())

        // Act
        val result = repository.selectedLanguageId.first()

        // Assert
        assertEquals(defaultLanguageId, result)
    }

    @Test
    fun `selectedLanguageId returns stored value when set`() = runTest {
        // Arrange
        val storedLanguageId = 2
        val preferencesKey = intPreferencesKey("selected_language_id")
        val preferences = preferencesOf(preferencesKey to storedLanguageId)
        every { dataStore.data } returns flowOf(preferences)

        // Act
        val result = repository.selectedLanguageId.first()

        // Assert
        assertEquals(storedLanguageId, result)
    }

    @Test
    fun `updateSelectedLanguageId updates the stored language ID`() = runTest {
        // Arrange
        val languageId = 2
        val editSlot = slot<suspend (MutablePreferences) -> Unit>()
        coEvery { dataStore.edit(capture(editSlot)) } returns preferencesOf()

        // Act
        repository.updateSelectedLanguageId(languageId)

        // Assert
        coVerify { dataStore.edit(any()) }
    }
} 