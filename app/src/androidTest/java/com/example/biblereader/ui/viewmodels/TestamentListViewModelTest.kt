package com.example.biblereader.ui.viewmodels

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.biblereader.repository.AppRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestamentListViewModelTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: AppRepository

    private lateinit var viewModel: TestamentListViewModel

    @Before
    fun setup() {
        hiltRule.inject()
        viewModel = TestamentListViewModel()
    }

    @Test
    fun dataRetrievalFromRepositoryIsAccurate() = runTest {
        // Given
        val expectedTestamentCount = 2 // Old and New Testament

        // When
        val testaments = viewModel.testaments.first()

        // Then
        assert(testaments.size == expectedTestamentCount)
        assert(testaments.any { it.name == "Isezerano rya Kera" })
        assert(testaments.any { it.name == "Isezerano Rishya" })
    }

    @Test
    fun repositoryGetBooksByTestamentIsCorrect() = runTest {
        // Given
        val testamentName = "Isezerano rya Kera"

        // When
        val books = repository.getBooksByTestament(testamentName).first()

        // Then
        assert(books.isNotEmpty())
        assert(books.any { it.testament == testamentName })
    }

    @Test
    fun repositoryGetBooksByTestamentForNewTestamentIsCorrect() = runTest {
        // Given
        val testamentName = "Isezerano Rishya"

        // When
        val books = repository.getBooksByTestament(testamentName).first()

        // Then
        assert(books.isNotEmpty())
        assert(books.any { it.testament == testamentName })
        assert(books.any { it.name == "Matayo" })
        assert(books.any { it.name == "Yohana" })
    }
} 