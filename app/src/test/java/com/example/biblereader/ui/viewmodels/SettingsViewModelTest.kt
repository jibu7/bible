package com.example.biblereader.ui.viewmodels

import com.example.biblereader.data.local.db.entities.LanguageEntity
import com.example.biblereader.data.local.preferences.ThemePreferences
import com.example.biblereader.data.preferences.UserPreferencesRepository
import com.example.biblereader.repository.AppRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private val repository: AppRepository = mockk()
    private val themePreferences: ThemePreferences = mockk()
    private val userPreferencesRepository: UserPreferencesRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SettingsViewModel(
            repository = repository,
            themePreferences = themePreferences,
            userPreferencesRepository = userPreferencesRepository
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `availableLanguages returns flow of languages from repository`() = runTest {
        // Arrange
        val expectedLanguages = listOf(
            LanguageEntity(languageId = 1, languageName = "English", languageCode = "en"),
            LanguageEntity(languageId = 2, languageName = "Kinyarwanda", languageCode = "rw")
        )
        every { repository.getAllLanguages() } returns flowOf(expectedLanguages)

        // Act
        val result = viewModel.availableLanguages.value

        // Assert
        assertEquals(expectedLanguages, result)
    }

    @Test
    fun `selectedLanguageId returns flow from userPreferencesRepository`() = runTest {
        // Arrange
        val expectedLanguageId = 2
        every { userPreferencesRepository.selectedLanguageId } returns flowOf(expectedLanguageId)

        // Act
        val result = viewModel.selectedLanguageId.value

        // Assert
        assertEquals(expectedLanguageId, result)
    }

    @Test
    fun `isDarkModeForced returns flow from themePreferences`() = runTest {
        // Arrange
        every { themePreferences.isDarkModeForced } returns flowOf(true)

        // Act
        val result = viewModel.isDarkModeForced.value

        // Assert
        assertTrue(result)
    }

    @Test
    fun `isDarkModeEnabled returns flow from themePreferences`() = runTest {
        // Arrange
        every { themePreferences.isDarkModeEnabled } returns flowOf(true)

        // Act
        val result = viewModel.isDarkModeEnabled.value

        // Assert
        assertTrue(result)
    }

    @Test
    fun `onLanguageSelected calls userPreferencesRepository updateSelectedLanguageId`() = runTest {
        // Arrange
        val languageId = 2
        coEvery { userPreferencesRepository.updateSelectedLanguageId(languageId) } returns Unit

        // Act
        viewModel.onLanguageSelected(languageId)

        // Assert
        coVerify { userPreferencesRepository.updateSelectedLanguageId(languageId) }
    }

    @Test
    fun `setDarkModeForced calls themePreferences setDarkModeForced`() = runTest {
        // Arrange
        val isForced = true
        coEvery { themePreferences.setDarkModeForced(isForced) } returns Unit

        // Act
        viewModel.setDarkModeForced(isForced)

        // Assert
        coVerify { themePreferences.setDarkModeForced(isForced) }
    }

    @Test
    fun `setDarkModeEnabled calls themePreferences setDarkModeEnabled`() = runTest {
        // Arrange
        val isEnabled = true
        coEvery { themePreferences.setDarkModeEnabled(isEnabled) } returns Unit

        // Act
        viewModel.setDarkModeEnabled(isEnabled)

        // Assert
        coVerify { themePreferences.setDarkModeEnabled(isEnabled) }
    }
} 