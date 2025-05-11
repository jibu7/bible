package com.example.biblereader // Or your actual test package path

import androidx.test.ext.junit.runners.AndroidJUnit4 // Often used, but RobolectricTestRunner takes precedence via @RunWith
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner // Standard Robolectric runner
import org.robolectric.annotation.Config

// 1. Use the standard Robolectric runner
@RunWith(RobolectricTestRunner::class)
// 2. Configure Robolectric: Specify HiltTestApplication and optionally SDK
@Config(application = HiltTestApplication::class, sdk = [28]) // Adjust SDK as needed
// 3. Enable Hilt features for this test
@HiltAndroidTest
class ExampleInstrumentedTest {

    // 4. Hilt Rule: Manages the component's state
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // 5. Inject dependencies into the test class itself before each test
        hiltRule.inject()
    }

    @Test
    fun exampleTest() {
        // Your test logic here
        // You can now use @Inject to get dependencies provided by Hilt
        // e.g., @Inject lateinit var myViewModel: MyViewModel
        // assertNotNull(myViewModel)
    }

    // Add other tests as needed
} 